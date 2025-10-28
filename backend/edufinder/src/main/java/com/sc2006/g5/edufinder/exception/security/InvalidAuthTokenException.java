package com.sc2006.g5.edufinder.exception.security;

import org.springframework.security.core.AuthenticationException;

public class InvalidAuthTokenException extends AuthenticationException {
    public InvalidAuthTokenException(String message){
        super(message);
    }
}
