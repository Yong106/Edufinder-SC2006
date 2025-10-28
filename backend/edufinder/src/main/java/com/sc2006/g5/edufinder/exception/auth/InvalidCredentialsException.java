package com.sc2006.g5.edufinder.exception.auth;

import org.springframework.http.HttpStatus;

import com.sc2006.g5.edufinder.exception.common.ApiException;

public class InvalidCredentialsException extends ApiException{
    public InvalidCredentialsException(){
        super("Wrong username or password.", HttpStatus.BAD_REQUEST);
    }
}
