package com.sc2006.g5.edufinder.security;

public interface SessionProvider {

    String generateToken(Long userId);
    Long validateAndGetUserId(String token);

} 
