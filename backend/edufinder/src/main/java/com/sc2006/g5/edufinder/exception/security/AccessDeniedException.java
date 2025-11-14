package com.sc2006.g5.edufinder.exception.security;

import org.springframework.http.HttpStatus;

import com.sc2006.g5.edufinder.exception.common.ApiException;

/**
 * Thrown when a user attempts to access a resource or perform an action
 * they do not have permission for.
 * <p>
 * Returns HTTP 404 Not Found.
 */
public class AccessDeniedException extends ApiException {

    /**
     * Creates a new exception indicating that access is denied.
     */
    public AccessDeniedException(){
        super("Access denied.", HttpStatus.NOT_FOUND);
    }

    /**
     * Creates a new exception with a custom access-denied message.
     *
     * @param message a custom error message describing the denial reason
     */
    public AccessDeniedException(String message){
        super(message, HttpStatus.NOT_FOUND);
    }
}
