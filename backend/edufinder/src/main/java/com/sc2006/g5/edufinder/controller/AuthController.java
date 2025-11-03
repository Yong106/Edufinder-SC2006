package com.sc2006.g5.edufinder.controller;

import com.sc2006.g5.edufinder.dto.response.UserResponse;
import com.sc2006.g5.edufinder.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.auth.cookie.name}")
    private String cookieName;

    @Value("${app.auth.cookie.expiration}")
    private Long cookieExpiration;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request);

        ResponseCookie cookie = createCookie(token, cookieExpiration);
        UserResponse user = userService.getUserByUsername(request.getUsername());

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(user);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        String token = authService.signup(request);

        ResponseCookie cookie = createCookie(token, cookieExpiration);
        UserResponse user = userService.getUserByUsername(request.getUsername());

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = createCookie(null, 0);

        return ResponseEntity.noContent()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .build();
    }

    private ResponseCookie createCookie(String token, long maxAge) {
         return ResponseCookie.from(cookieName, token)
                .httpOnly(true)         // prevent JS access
                .secure(true)           // only send over HTTPS
                .path("/")              // send for all endpoints
                .maxAge(maxAge)
                .sameSite("Strict")     // avoid CSRF from other sites
                .build();
    }
}
