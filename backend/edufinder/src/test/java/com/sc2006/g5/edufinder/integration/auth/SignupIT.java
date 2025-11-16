package com.sc2006.g5.edufinder.integration.auth;

import com.sc2006.g5.edufinder.model.user.Role;
import com.sc2006.g5.edufinder.model.user.User;
import com.sc2006.g5.edufinder.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SignupIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.auth.cookie.name}")
    private String cookieName;

    private static final String STRONG_PASSWORD = "P@ssw0rd";

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
    @DisplayName("should create user and return 200 with user response when request valid")
    void shouldCreateUserAndReturn200WithUserResponseWhenRequestValid() throws Exception {
        String username = "RandomUser";

        mockMvc.perform(mockRequest(username, STRONG_PASSWORD))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.role").value(Role.USER.toString()))
                .andExpect(header().string("Set-Cookie", containsString(cookieName)));

        boolean exists = userRepository.findOneByUsername(username).isPresent();
        assertTrue(exists);
    }

    @Test
    @DisplayName("should return 400 when missing username")
    void shouldReturn400WhenMissingUsername() throws Exception {
        mockMvc.perform(mockRawRequest("""
            {"password": "password"}))"}
        """)).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 409 when duplicate username")
    void shouldReturn409WhenDuplicateUsername() throws Exception {
        String username = "AnotherUser";

        userRepository.save(User.builder()
                .username(username)
                .password(passwordEncoder.encode("P@ssw0rd"))
                .build());

        mockMvc.perform(mockRequest(username, STRONG_PASSWORD))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("should return 400 when invalid password")
    void signup_weakPassword1() throws Exception {
        String username = "weakPasswordUser";

        String noLowerCasePassword = "ABCD123@";
        String noUpperCasePassword = "abcd123@";
        String noDigitPassword = "Abcdefg@";
        String noSpecialCharacterPassword = "Abcd1234";
        String shortPassword = "Abab12@";

        mockMvc.perform(mockRequest(username, noLowerCasePassword))
                .andExpect(status().isBadRequest());

        mockMvc.perform(mockRequest(username, noUpperCasePassword))
                .andExpect(status().isBadRequest());

        mockMvc.perform(mockRequest(username, noDigitPassword))
                .andExpect(status().isBadRequest());

        mockMvc.perform(mockRequest(username, noSpecialCharacterPassword))
                .andExpect(status().isBadRequest());

        mockMvc.perform(mockRequest(username, shortPassword))
                .andExpect(status().isBadRequest());
    }
}