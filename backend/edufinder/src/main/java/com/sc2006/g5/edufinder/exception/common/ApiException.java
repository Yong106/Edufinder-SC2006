package com.sc2006.g5.edufinder.exception.common;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
     
    private final HttpStatus httpStatus;

    protected ApiException(String message, HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
