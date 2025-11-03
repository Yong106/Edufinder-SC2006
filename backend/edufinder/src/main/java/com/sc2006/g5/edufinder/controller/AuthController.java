package com.sc2006.g5.edufinder.controller;

import com.sc2006.g5.edufinder.dto.response.UserResponse;
import com.sc2006.g5.edufinder.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sc2006.g5.edufinder.dto.request.LoginRequest;
import com.sc2006.g5.edufinder.dto.request.SignupRequest;
import com.sc2006.g5.edufinder.service.AuthService;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);

        return createResponseEntity(token, request.getUsername());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        String token = authService.signup(request);

        return createResponseEntity(token, request.getUsername());
    }

    private ResponseEntity<?> createResponseEntity(String token, String username) {
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)         // prevent JS access
                .secure(true)           // only send over HTTPS
                .path("/")              // send for all endpoints
                .maxAge(24 * 60 * 60)   // 1 day
                .sameSite("Strict")     // avoid CSRF from other sites
                .build();

        UserResponse user = userService.getUserByUsername(username);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(user);
    }
}
