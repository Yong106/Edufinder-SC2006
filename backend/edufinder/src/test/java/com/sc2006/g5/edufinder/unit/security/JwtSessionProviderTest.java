package com.sc2006.g5.edufinder.unit.security;

import com.sc2006.g5.edufinder.exception.security.InvalidAuthTokenException;
import com.sc2006.g5.edufinder.security.JwtSessionProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JwtSessionProviderTest {

    private static final String KEY = "mysecretkeymysecretkeymysecretkeymyse";
    private static final long EXPIRATION_TIME = 1000L;

    private static final long USER_ID = 106L;

    @Test
    @DisplayName("should generate valid token")
    void shouldGenerateValidToken() {
        JwtSessionProvider jwtSessionProvider = new JwtSessionProvider(KEY, EXPIRATION_TIME);

        String token = jwtSessionProvider.generateToken(USER_ID);
        Long userId = jwtSessionProvider.validateAndGetUserId(token);

        assertEquals(USER_ID, userId);
    }

    @Test
    @DisplayName("should throw when invalid token")
    void shouldThrowWhenInvalidToken() {
        JwtSessionProvider jwtSessionProvider = new JwtSessionProvider(KEY, EXPIRATION_TIME);
        assertThrows(InvalidAuthTokenException.class, () ->
            jwtSessionProvider.validateAndGetUserId("invalid")
        );
    }

    @Test
    @DisplayName("should throw when token expired")
    void shouldThrowWhenExpiredToken() throws InterruptedException {
        JwtSessionProvider jwtSessionProvider = new JwtSessionProvider(KEY, 1);

        String token = jwtSessionProvider.generateToken(USER_ID);
        Thread.sleep(5);

        assertThrows(InvalidAuthTokenException.class, () ->
            jwtSessionProvider.validateAndGetUserId(token)
        );
    }

}
