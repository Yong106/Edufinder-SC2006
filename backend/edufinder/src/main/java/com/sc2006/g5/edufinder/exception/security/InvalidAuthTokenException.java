package com.sc2006.g5.edufinder.exception.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Thrown when an authentication token is invalid, malformed, expired,
 * or otherwise unable to be processed for authentication.
 */
public class InvalidAuthTokenException extends AuthenticationException {
    /**
     * Creates a new exception indicating that the provided authentication token
     * is invalid.
     *
     * @param message the error message describing why the token is invalid
     */
    public InvalidAuthTokenException(String message){
        super(message);
    }
}
