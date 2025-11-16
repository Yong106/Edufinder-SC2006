package com.sc2006.g5.edufinder.exception.auth;

import org.springframework.http.HttpStatus;

import com.sc2006.g5.edufinder.exception.common.ApiException;

/**
 * Thrown when a login attempt fail due to username not found or unmatched password.
 * <p>
 * Returns HTTP 401 Unauthorized.
 */
public class InvalidCredentialsException extends ApiException{

    /**
     * Creates a new exception indicating incorrect username or password.
     */
    public InvalidCredentialsException(){
        super("Incorrect username or password.", HttpStatus.UNAUTHORIZED);
    }
}
