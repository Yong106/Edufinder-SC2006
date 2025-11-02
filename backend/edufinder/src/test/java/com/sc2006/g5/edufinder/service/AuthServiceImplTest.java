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

    private final String newUsername = "user";
    private final String existedUsername = "duplicateUser";

    private final Long newUserId = 2L;
    private final Long existedUserId = 1L;

    private final String validPassword = "Abcd1234@";
    private final String encodedPassword = "encodedPassword";

    private final String newUserToken = "newUserToken";
    private final String existedUserToken = "existedUserToken";

    @BeforeEach
    void setup(){
        User existedUser = User.builder()
            .id(existedUserId)
            .username(existedUsername)
            .password(encodedPassword)
            .createdAt(LocalDateTime.now())
            .build();
        
        lenient().when(userRepository.findOneByUsername(newUsername))
            .thenReturn(Optional.empty());

        lenient().when(userRepository.findOneByUsername(existedUsername))
            .thenReturn(Optional.of(existedUser));

        lenient().when(passwordEncoder.matches(anyString(), anyString())).thenAnswer(invocation -> {
            String rawPassword = invocation.getArgument(0);
            String encodedRawPassword = invocation.getArgument(1);
            return rawPassword.equals(validPassword) && encodedRawPassword.equals(encodedPassword);
        });
    }

    @Nested
    @DisplayName("register()")
    class SignupTests {

        @Test
        @DisplayName("should save new user and return auth token when request valid")
        void shouldReturnAuthTokenWhenRequestValid(){
            when(userRepository.save(argThat(user -> 
                user.getUsername().equals(newUsername) &&
                user.getPassword().equals(encodedPassword)
            ))).thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setId(newUserId);
                return user;
            });

            when(passwordEncoder.encode(validPassword))
                .thenReturn(encodedPassword);

            when(sessionProvider.generateToken(newUserId))
                .thenReturn(newUserToken);
            
            SignupRequest request = SignupRequest.builder()
                .username(newUsername)
                .password(validPassword)
                .build();
            
            String token = authServiceImpl.signup(request);

            assertEquals(token, newUserToken);

            verify(userRepository, times(1)).save(any());
            verify(passwordEncoder, times(1)).encode(any());
            verify(sessionProvider, times(1)).generateToken(any());
        }

        @Test
        @DisplayName("should throw when username existed")
        void shouldThrowWhenUsernameExisted(){
            SignupRequest request = SignupRequest.builder()
                .username(existedUsername)
                .password(validPassword)
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
                .username(existedUsername)
                .password(validPassword)
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

        @Test
        @DisplayName("should return auth token when request valid")
        void shouldReturnAuthTokenWhenRequestValid(){
            when(sessionProvider.generateToken(existedUserId))
                .thenReturn(existedUserToken);

            LoginRequest request = LoginRequest.builder()
                .username(existedUsername)
                .password(validPassword)
                .build();
            
            String token = authServiceImpl.login(request);

            assertEquals(token, existedUserToken);
            
            verify(passwordEncoder, times(1)).matches(any(), any());
            verify(sessionProvider, times(1)).generateToken(any());
        }

        @Test
        @DisplayName("should throw when user not found")
        void shouldThrowWhenUsernameExisted(){
            LoginRequest request = LoginRequest.builder()
                .username(newUsername)
                .password(validPassword)
                .build();
            
            assertThrowsExactly(InvalidCredentialsException.class, () -> authServiceImpl.login(request));
            
            verify(passwordEncoder, never()).matches(any(), any());
            verify(sessionProvider, never()).generateToken(any());
        }

        @Test
        @DisplayName("should throw when password not matched")
        void shouldThrowWhenPasswordNotMatched(){
            LoginRequest request = LoginRequest.builder()
                .username(existedUsername)
                .password("wrongPassword")
                .build();
            
            assertThrowsExactly(InvalidCredentialsException.class, () -> authServiceImpl.login(request));
            
            verify(passwordEncoder, times(1)).matches(any(), any());
            verify(sessionProvider, never()).generateToken(any());
        }

    }
    
    
}
