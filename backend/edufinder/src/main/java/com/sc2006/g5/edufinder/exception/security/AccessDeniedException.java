package com.sc2006.g5.edufinder.exception.security;

import org.springframework.http.HttpStatus;

import com.sc2006.g5.edufinder.exception.common.ApiException;

public class AccessDeniedException extends ApiException {
    public AccessDeniedException(){
        super("Access denied.", HttpStatus.NOT_FOUND);
    }

    public AccessDeniedException(String message){
        super(message, HttpStatus.NOT_FOUND);
    }
}
