package com.sc2006.g5.edufinder.exception.util;

import org.springframework.http.HttpStatus;

import com.sc2006.g5.edufinder.exception.common.ApiException;

/**
 * Thrown when a JSON string cannot be parsed into the expected object.
 * <p>
 * Typically indicates malformed JSON, incompatible structure, or unexpected data.
 * <p>
 * Returns HTTP 500 Internal Server Error.
 */
public class JsonParsingException extends ApiException{

    /**
     * Creates a new exception indicating that a JSON parsing failure occurred.
     *
     * @param message the error message describing the parsing issue
     */
    public JsonParsingException(String message){
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}