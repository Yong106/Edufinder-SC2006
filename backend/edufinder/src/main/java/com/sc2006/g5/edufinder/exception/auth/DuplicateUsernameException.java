package com.sc2006.g5.edufinder.exception.auth;

import org.springframework.http.HttpStatus;

import com.sc2006.g5.edufinder.exception.common.ApiException;

/**
 * Thrown when a signup attempt uses a username that already exists.
 * <p>
 * Returns HTTP 409 Conflict.
 */
public class DuplicateUsernameException extends ApiException {

    /**
     * Creates a new exception indicating username already exists.
     */
    public DuplicateUsernameException(){
        super("Username existed.", HttpStatus.CONFLICT);
    }
}
