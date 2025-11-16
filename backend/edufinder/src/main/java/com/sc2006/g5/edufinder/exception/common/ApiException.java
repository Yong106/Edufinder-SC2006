package com.sc2006.g5.edufinder.exception.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base class for custom API exceptions used to represent HTTP error conditions.
 * <p>
 * Each exception carries an associated {@link HttpStatus}, allowing the application
 * to produce consistent and meaningful HTTP responses.
 */
@Getter
public class ApiException extends RuntimeException {
     
    private final HttpStatus httpStatus;

    /**
     * Creates a new API exception with the given error message and HTTP status.
     *
     * @param message the error message describing the failure
     * @param httpStatus the HTTP status to associate with this exception
     */
    public ApiException(String message, HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }

}
