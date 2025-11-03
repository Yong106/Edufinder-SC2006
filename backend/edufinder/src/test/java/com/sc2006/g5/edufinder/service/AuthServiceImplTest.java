package com.sc2006.g5.edufinder.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sc2006.g5.edufinder.dto.request.LoginRequest;
import com.sc2006.g5.edufinder.dto.request.SignupRequest;
import com.sc2006.g5.edufinder.exception.auth.DuplicateUsernameException;
import com.sc2006.g5.edufinder.exception.auth.InvalidCredentialsException;
import com.sc2006.g5.edufinder.model.user.User;
import com.sc2006.g5.edufinder.repository.UserRepository;
import com.sc2006.g5.edufinder.security.SessionProvider;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthServiceImpl Tests")
public class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SessionProvider sessionProvider;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    private static final String NEW_USERNAME = "user";
    private static final String EXISTED_USERNAME = "duplicateUser";

    private static final Long NEW_USER_ID = 2L;
    private static final Long EXISTED_USER_ID = 1L;

    private static final String VALID_PASSWORD = "Abcd1234@";
    private static final String ENCODED_PASSWORD = "encoded_password";

    private static final String NEW_USER_TOKEN = "new_user_token";
    private static final String EXISTED_USER_TOKEN = "existed_user_token";

    @BeforeEach
    void setup(){
        User existedUser = User.builder()
            .id(EXISTED_USER_ID)
            .username(EXISTED_USERNAME)
            .password(ENCODED_PASSWORD)
            .createdAt(LocalDateTime.now())
            .build();
        
        lenient().when(userRepository.findOneByUsername(NEW_USERNAME))
            .thenReturn(Optional.empty());

        lenient().when(userRepository.findOneByUsername(EXISTED_USERNAME))
            .thenReturn(Optional.of(existedUser));
    }

    @Nested
    @DisplayName("signup()")
    class SignupTests {

        @Test
        @DisplayName("should save new user and return auth token when request valid")
        void shouldReturnAuthTokenWhenRequestValid(){
            when(userRepository.save(argThat(user -> 
                user.getUsername().equals(NEW_USERNAME) &&
                user.getPassword().equals(ENCODED_PASSWORD)
            ))).thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setId(NEW_USER_ID);
                return user;
            });

            when(passwordEncoder.encode(VALID_PASSWORD))
                .thenReturn(ENCODED_PASSWORD);

            when(sessionProvider.generateToken(NEW_USER_ID))
                .thenReturn(NEW_USER_TOKEN);
            
            SignupRequest request = SignupRequest.builder()
                .username(NEW_USERNAME)
                .password(VALID_PASSWORD)
                .build();
            
            String token = authServiceImpl.signup(request);

            assertEquals(NEW_USER_TOKEN, token);

            verify(userRepository, times(1)).save(any());
            verify(passwordEncoder, times(1)).encode(any());
            verify(sessionProvider, times(1)).generateToken(any());
        }

        @Test
        @DisplayName("should throw when username existed")
        void shouldThrowWhenUsernameExisted(){
            SignupRequest request = SignupRequest.builder()
                .username(EXISTED_USERNAME)
                .password(VALID_PASSWORD)
                .build();
            
            assertThrowsExactly(DuplicateUsernameException.class, () -> authServiceImpl.signup(request));
            
            verify(userRepository, never()).save(any());
            verify(passwordEncoder, never()).encode(any());
            verify(sessionProvider, never()).generateToken(any());
        }

        @Test
        @DisplayName("should throw when password invalid")
        void shouldThrowWhenPasswordInvalid(){
            SignupRequest request = SignupRequest.builder()
                .username(EXISTED_USERNAME)
                .password(VALID_PASSWORD)
                .build();

            String noLowerCasePassword = "ABCD123@";
            String noUpperCasePassword = "abcd123@";
            String noDigitPassword = "Abcdefg@";
            String noSpecialCharacterPassword = "Abcd1234";
            String shortPassword = "Abab12@";
            
            assertThrowsExactly(DuplicateUsernameException.class, () -> {
                request.setPassword(noLowerCasePassword);
                authServiceImpl.signup(request);
            });

            assertThrowsExactly(DuplicateUsernameException.class, () -> {
                request.setPassword(noUpperCasePassword);
                authServiceImpl.signup(request);
            });

            assertThrowsExactly(DuplicateUsernameException.class, () -> {
                request.setPassword(noDigitPassword);
                authServiceImpl.signup(request);
            });

            assertThrowsExactly(DuplicateUsernameException.class, () -> {
                request.setPassword(noSpecialCharacterPassword);
                authServiceImpl.signup(request);
            });

            assertThrowsExactly(DuplicateUsernameException.class, () -> {
                request.setPassword(shortPassword);
                authServiceImpl.signup(request);
            });

            verify(userRepository, never()).save(any());
            verify(passwordEncoder, never()).encode(any());
            verify(sessionProvider, never()).generateToken(any());
        }

    }

    @Nested
    @DisplayName("login()")
    class LoginTests {

        @BeforeEach
        void setup(){
            lenient().when(passwordEncoder.matches(anyString(), anyString())).thenAnswer(invocation -> {
                String rawPassword = invocation.getArgument(0);
                String encodedRawPassword = invocation.getArgument(1);
                return rawPassword.equals(VALID_PASSWORD) && encodedRawPassword.equals(ENCODED_PASSWORD);
            });
        }

        @Test
        @DisplayName("should return auth token when request valid")
        void shouldReturnAuthTokenWhenRequestValid(){
            when(sessionProvider.generateToken(EXISTED_USER_ID))
                .thenReturn(EXISTED_USER_TOKEN);

            LoginRequest request = LoginRequest.builder()
                .username(EXISTED_USERNAME)
                .password(VALID_PASSWORD)
                .build();
            
            String token = authServiceImpl.login(request);

            assertEquals(EXISTED_USER_TOKEN, token);
            
            verify(passwordEncoder, times(1)).matches(any(), any());
            verify(sessionProvider, times(1)).generateToken(any());
        }

        @Test
        @DisplayName("should throw when user not found")
        void shouldThrowWhenUsernameExisted(){
            LoginRequest request = LoginRequest.builder()
                .username(NEW_USERNAME)
                .password(VALID_PASSWORD)
                .build();
            
            assertThrowsExactly(InvalidCredentialsException.class, () -> authServiceImpl.login(request));
            
            verify(passwordEncoder, never()).matches(any(), any());
            verify(sessionProvider, never()).generateToken(any());
        }

        @Test
        @DisplayName("should throw when password not matched")
        void shouldThrowWhenPasswordNotMatched(){
            LoginRequest request = LoginRequest.builder()
                .username(EXISTED_USERNAME)
                .password("wrongPassword")
                .build();
            
            assertThrowsExactly(InvalidCredentialsException.class, () -> authServiceImpl.login(request));
            
            verify(passwordEncoder, times(1)).matches(any(), any());
            verify(sessionProvider, never()).generateToken(any());
        }

    }
    
    
}
