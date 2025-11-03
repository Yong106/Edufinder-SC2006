package com.sc2006.g5.edufinder.controller;

import com.sc2006.g5.edufinder.config.GlobalExceptionHandler;
import com.sc2006.g5.edufinder.config.SecurityConfig;
import com.sc2006.g5.edufinder.dto.response.UserResponse;
import com.sc2006.g5.edufinder.exception.auth.DuplicateUsernameException;
import com.sc2006.g5.edufinder.exception.auth.InvalidCredentialsException;
import com.sc2006.g5.edufinder.exception.auth.InvalidPasswordException;
import com.sc2006.g5.edufinder.security.AuthFilter;
import com.sc2006.g5.edufinder.service.AuthService;
import com.sc2006.g5.edufinder.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        AuthFilter.class,
        SecurityConfig.class
    })
})
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserService userService;

    private final String EXISTED_USERNAME = "valid_username";
    private final String NEW_USERNAME = "new_username";

    private final String CORRECT_PASSWORD = "user_password";
    private final String WRONG_PASSWORD = "wrong_password";
    private final String INVALID_PASSWORD = "invalid_password";

    private final String TOKEN = "token";

    @Value("${app.auth.cookie.name}")
    private String cookieName;

    @Value("${app.auth.cookie.expiration}")
    private Long cookieExpiration;

    @Nested
    @DisplayName("POST /api/auth/login")
    class LoginTest {

        private MockHttpServletRequestBuilder mockLoginRequest(String content) {
            return post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        }

        private MockHttpServletRequestBuilder mockLoginRequest(String username, String password) {
            return mockLoginRequest("""
                    {"username": "%s", "password": "%s"}
                """.formatted(username, password));
        }

        @Test
        @DisplayName("should return 200 with user response when request valid")
        void shouldReturn200WithUserResponseWhenRequestValid() throws Exception {
            UserResponse userResponse = UserResponse.builder()
                .username(EXISTED_USERNAME)
                .build();

            when(authService.login(argThat(request ->
                request.getUsername().equals(EXISTED_USERNAME) &&
                request.getPassword().equals(CORRECT_PASSWORD)
            ))).thenReturn(TOKEN);

            when(userService.getUserByUsername(EXISTED_USERNAME))
                .thenReturn(userResponse);

            mockMvc.perform(mockLoginRequest(EXISTED_USERNAME, CORRECT_PASSWORD))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("%s=%s".formatted(cookieName, TOKEN))))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Max-Age=%d".formatted(cookieExpiration))))
                .andExpect(jsonPath("$.username").value(EXISTED_USERNAME));

            verify(authService, times(1)).login(any());
            verify(userService, times(1)).getUserByUsername(any());
        }

        @Test
        @DisplayName("should return 400 when request malformed")
        void shouldReturn400WhenRequestMalformed() throws Exception {
            mockMvc.perform(mockLoginRequest("{)"))
                .andExpect(status().isBadRequest());

            mockMvc.perform(mockLoginRequest("""
                {"username": "1"}
            """)).andExpect(status().isBadRequest());

            mockMvc.perform(mockLoginRequest("""
                {"password": "1"}
            """)).andExpect(status().isBadRequest());

            verify(authService, never()).login(any());
            verify(userService, never()).getUserByUsername(any());
        }

        @Test
        @DisplayName("should return 401 when invalid credentials")
        void shouldReturn401WhenInvalidCredentials() throws Exception {
            when(authService.login(argThat(request ->
                request.getUsername().equals(EXISTED_USERNAME) &&
                request.getPassword().equals(WRONG_PASSWORD)
            ))).thenAnswer((invocation) -> {
                throw new InvalidCredentialsException();
            });

            mockMvc.perform(mockLoginRequest(EXISTED_USERNAME, WRONG_PASSWORD))
                .andExpect(status().isUnauthorized());

            verify(authService, times(1)).login(any());
            verify(userService, never()).getUserByUsername(any());
        }
    }

    @Nested
    @DisplayName("POST /api/auth/signup")
    class SignupTest {

        private MockHttpServletRequestBuilder mockSignupRequest(String content) {
            return post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        }

        private MockHttpServletRequestBuilder mockSignupRequest(String username, String password) {
            return mockSignupRequest("""
                {"username": "%s", "password": "%s"}
            """.formatted(username, password));
        }

        @Test
        @DisplayName("should return 200 with user response when request valid")
        void shouldReturn200WithUserResponseWhenRequestValid() throws Exception {
            UserResponse userResponse = UserResponse.builder()
                .username(NEW_USERNAME)
                .build();

            when(authService.signup(argThat(request ->
                request.getUsername().equals(NEW_USERNAME) &&
                request.getPassword().equals(CORRECT_PASSWORD)
            ))).thenReturn(TOKEN);

            when(userService.getUserByUsername(NEW_USERNAME))
                .thenReturn(userResponse);

            mockMvc.perform(mockSignupRequest(NEW_USERNAME, CORRECT_PASSWORD))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("%s=%s".formatted(cookieName, TOKEN))))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Max-Age=%d".formatted(cookieExpiration))))
                .andExpect(jsonPath("$.username").value(NEW_USERNAME));

            verify(authService, times(1)).signup(any());
            verify(userService, times(1)).getUserByUsername(any());
        }

        @Test
        @DisplayName("should return 400 when request malformed")
        void shouldReturn400WhenRequestMalformed() throws Exception {
            mockMvc.perform(mockSignupRequest("{)"))
                .andExpect(status().isBadRequest());

            mockMvc.perform(mockSignupRequest("""
                {"username": "1"}
            """)).andExpect(status().isBadRequest());

            mockMvc.perform(mockSignupRequest("""
                {"password": "1"}
            """)).andExpect(status().isBadRequest());

            verify(authService, never()).signup(any());
            verify(userService, never()).getUserByUsername(any());
        }

        @Test
        @DisplayName("should return 409 when duplicate username")
        void shouldReturn409WhenDuplicateUsername() throws Exception {
            when(authService.signup(argThat(request ->
                request.getUsername().equals(EXISTED_USERNAME) &&
                request.getPassword().equals(CORRECT_PASSWORD)
            ))).thenAnswer((invocation) -> {
                throw new DuplicateUsernameException();
            });

            mockMvc.perform(mockSignupRequest(EXISTED_USERNAME, CORRECT_PASSWORD))
                .andExpect(status().isConflict());

            verify(authService, times(1)).signup(any());
            verify(userService, never()).getUserByUsername(any());
        }

        @Test
        @DisplayName("should return 400 when invalid password")
        void shouldReturn400WhenInvalidPassword() throws Exception {
            when(authService.signup(argThat(request ->
                request.getUsername().equals(NEW_USERNAME) &&
                request.getPassword().equals(INVALID_PASSWORD)
            ))).thenAnswer((invocation) -> {
                throw new InvalidPasswordException();
            });

            mockMvc.perform(mockSignupRequest(NEW_USERNAME, INVALID_PASSWORD))
                .andExpect(status().isBadRequest());

            verify(authService, times(1)).signup(any());
            verify(userService, never()).getUserByUsername(any());
        }
    }

    @Nested
    @DisplayName("POST /api/auth/logout")
    class LogoutTest {

        @Test
        @DisplayName("should clear cookie when request valid")
        void shouldClearCookieWhenRequestValid() throws Exception {
            mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Max-Age=0")));
        }
    }
}
