package com.sc2006.g5.edufinder.security;

public interface SessionProvider {

    public String generateToken(Long userId); 
    public Long validateAndGetUserId(String token);

} 
