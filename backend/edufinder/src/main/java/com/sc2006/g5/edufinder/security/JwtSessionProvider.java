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

    @Value("${app.jwt.secret}")
    private String SECRET_KEY;
    @Value("${app.jwt.expiration}")
    private long EXPIRATION_TIME;

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

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
