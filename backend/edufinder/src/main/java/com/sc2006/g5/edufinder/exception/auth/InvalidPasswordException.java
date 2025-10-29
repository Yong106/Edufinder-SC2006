package com.sc2006.g5.edufinder.exception.auth;

import org.springframework.http.HttpStatus;

import com.sc2006.g5.edufinder.exception.common.ApiException;

public class InvalidPasswordException extends ApiException {
    public InvalidPasswordException(){
        super("Password too weak.", HttpStatus.BAD_REQUEST);
    }
}
