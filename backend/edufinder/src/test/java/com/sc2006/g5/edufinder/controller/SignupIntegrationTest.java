package com.sc2006.g5.edufinder.controller;

import com.sc2006.g5.edufinder.model.user.User;
import com.sc2006.g5.edufinder.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SuppressWarnings("null")
class SignupIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String json(String username, String password) {
        return "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
    }

    @Test
    @DisplayName("POST /api/auth/signup successfully")
    void signup_success() throws Exception {
        String username = "RandomUser";
        String password = "Abcd@1324"; // strong password

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(username, password)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value(username))
            .andExpect(header().string("Set-Cookie", containsString("jwt=")));

        // verify persistence
        boolean exists = userRepository.findOneByUsername(username).isPresent();
        org.junit.jupiter.api.Assertions.assertTrue(exists);
    }

    @Test
    @DisplayName("POST /api/auth/signup missing username -> 400")
    void signup_missingUsername() throws Exception {
        String body = "{\"password\":\"P@ssw0rd\"}";
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/signup duplicate username -> 409")
    void signup_duplicateUsername() throws Exception {
        String username = "AnotherUser";
        // pre-insert
        userRepository.save(User.builder()
                .username(username)
                .password(passwordEncoder.encode("P@ssw0rd"))
                .build());

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(username, "Abcd1234@")))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(409))
            .andExpect(jsonPath("$.message").value("Username existed."));
    }

    @Test
    @DisplayName("POST /api/auth/signup password without uppercase -> 400")
    void signup_weakPassword1() throws Exception {
        String username = "weakPasswordUser";
        // weak: no uppercase 
        String weak = "abcd@1234";
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(username, weak)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/signup password without lowercase -> 400")
    void signup_weakPassword2() throws Exception {
        String username = "weakPasswordUser2";
        // weak: no lowercase 
        String weak = "ABCD@1234";
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(username, weak)))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("POST /api/auth/signup password without number -> 400")
    void signup_weakPassword3() throws Exception {
        String username = "weakPasswordUser3";
        // weak: no number
        String weak = "ABCD@EFGHam";
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(username, weak)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/signup password without special character -> 400")
    void signup_weakPassword4() throws Exception {
        String username = "weakPasswordUser4";
        // weak: no special character
        String weak = "ABCD1234efgh";
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(username, weak)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/signup password too short -> 400")
    void signup_shortPassword() throws Exception {
        String username = "ShortPasswordUser";
        // short password
        String weak = "Word1@0";
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(username, weak)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/signup blank password -> 400")
    void signup_blankPassword() throws Exception {
        String username = "BlankPasswordUser";
        // blank password
        String blank = "";
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(username, blank)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/signup null username -> 400")
    void signup_nullUsername() throws Exception {
        String body = "{\"username\":null,\"password\":\"Abcd@1234\"}";
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/signup null password -> 400")
    void signup_nullPassword() throws Exception {
        String body = "{\"username\":\"NullPasswordUser\",\"password\":null}";
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/signup empty body -> 400")
    void signup_emptyBody() throws Exception {
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
            .andExpect(status().isBadRequest());
    }
}
