package com.sc2006.g5.edufinder.unit.controller;

import com.sc2006.g5.edufinder.config.GlobalExceptionHandler;
import com.sc2006.g5.edufinder.config.SecurityConfig;
import com.sc2006.g5.edufinder.controller.AuthController;
import com.sc2006.g5.edufinder.dto.response.UserResponse;
import com.sc2006.g5.edufinder.exception.auth.DuplicateUsernameException;
import com.sc2006.g5.edufinder.exception.auth.InvalidCredentialsException;
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

    private final String CORRECT_PASSWORD = "AbCd1234@";
    private final String WRONG_PASSWORD = "wrong_password";

    private final String TOKEN = "token";

    @Value("${app.auth.cookie.name}")
    private String cookieName;

    @Value("${app.auth.cookie.expiration}")
    private Long cookieExpiration;

    @Nested
    @DisplayName("POST /api/auth/login")
    class LoginTest {

        private MockHttpServletRequestBuilder mockRawRequest(String content) {
            return post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        }

        private MockHttpServletRequestBuilder mockRequest(String username, String password) {
            return mockRawRequest("""
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

            mockMvc.perform(mockRequest(EXISTED_USERNAME, CORRECT_PASSWORD))
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
            mockMvc.perform(mockRawRequest("{)"))
                .andExpect(status().isBadRequest());

            mockMvc.perform(mockRawRequest("""
                {"username": "1"}
            """)).andExpect(status().isBadRequest());

            mockMvc.perform(mockRawRequest("""
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

            mockMvc.perform(mockRequest(EXISTED_USERNAME, WRONG_PASSWORD))
                .andExpect(status().isUnauthorized());

            verify(authService, times(1)).login(any());
            verify(userService, never()).getUserByUsername(any());
        }
    }

    @Nested
    @DisplayName("POST /api/auth/signup")
    class SignupTest {

        private MockHttpServletRequestBuilder mockRawRequest(String content) {
            return post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        }

        private MockHttpServletRequestBuilder mockRequest(String username, String password) {
            return mockRawRequest("""
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

            mockMvc.perform(mockRequest(NEW_USERNAME, CORRECT_PASSWORD))
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
            mockMvc.perform(mockRawRequest("{)"))
                .andExpect(status().isBadRequest());

            mockMvc.perform(mockRawRequest("""
                {"username": "1"}
            """)).andExpect(status().isBadRequest());

            mockMvc.perform(mockRawRequest("""
                {"password": "1"}
            """)).andExpect(status().isBadRequest());

            verify(authService, never()).signup(any());
            verify(userService, never()).getUserByUsername(any());
        }

        @Test
        @DisplayName("should return 400 when invalid password")
        void shouldReturn400WhenInvalidPassword() throws Exception {
            String noLowerCasePassword = "ABCD123@";
            String noUpperCasePassword = "abcd123@";
            String noDigitPassword = "Abcdefg@";
            String noSpecialCharacterPassword = "Abcd1234";
            String shortPassword = "Abab12@";

            mockMvc.perform(mockRequest(EXISTED_USERNAME, noLowerCasePassword))
                .andExpect(status().isBadRequest());

            mockMvc.perform(mockRequest(EXISTED_USERNAME, noUpperCasePassword))
                .andExpect(status().isBadRequest());

            mockMvc.perform(mockRequest(EXISTED_USERNAME, noDigitPassword))
                .andExpect(status().isBadRequest());

            mockMvc.perform(mockRequest(EXISTED_USERNAME, noSpecialCharacterPassword))
                .andExpect(status().isBadRequest());

            mockMvc.perform(mockRequest(EXISTED_USERNAME, shortPassword))
                .andExpect(status().isBadRequest());

            verify(authService, never()).signup(any());
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

            mockMvc.perform(mockRequest(EXISTED_USERNAME, CORRECT_PASSWORD))
                .andExpect(status().isConflict());

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
