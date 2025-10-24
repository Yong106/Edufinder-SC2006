package com.sc2006.g5.edufinder.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sc2006.g5.edufinder.exception.InvalidJwtTokenException;

import java.security.Key;
import java.util.Date;

@Component
public class JwtSessionProvider implements SessionProvider {

    private final Key key;
    private final long expirationTime;

    public JwtSessionProvider(
        @Value("${app.jwt.secret}") String secretKey,
        @Value("${app.jwt.expiration}") long expirationTime
    ) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.expirationTime = expirationTime;
    }

    public String generateToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(Long.toString(userId))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Long validateAndGetUserId(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            String userId = claims.getSubject();
            return Long.parseLong(userId);
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtTokenException(token);
        }
    }
}
