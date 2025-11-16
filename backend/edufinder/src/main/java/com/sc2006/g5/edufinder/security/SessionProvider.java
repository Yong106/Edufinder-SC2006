package com.sc2006.g5.edufinder.security;

/**
 * A security class that provides methods to encode and decode authentication token.
 */
public interface SessionProvider {

    /**
     * Encodes {@code userId} into authentication token.
     *
     * @param userId the id of user to be encoded
     * @return an authentication token that can be decoded to get {@code userId}
     */
    String generateToken(Long userId);

    /**
     * Decodes authentication token into {@code userId}.
     *
     * @param token the authentication token be decoded
     * @return the {@code userId} contained in the authentication token
     *
     * @throws com.sc2006.g5.edufinder.exception.security.InvalidAuthTokenException if token is invalid of expired
     */
    Long validateAndGetUserId(String token);

} 
