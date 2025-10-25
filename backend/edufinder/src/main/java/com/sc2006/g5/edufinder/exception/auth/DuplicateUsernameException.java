package com.sc2006.g5.edufinder.exception.auth;

import org.springframework.http.HttpStatus;

import com.sc2006.g5.edufinder.exception.common.ApiException;

public class DuplicateUsernameException extends ApiException {
    public DuplicateUsernameException(){
        super("Username existed.", HttpStatus.CONFLICT);
    }
}
