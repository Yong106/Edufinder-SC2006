package com.sc2006.g5.edufinder.exception.user;

import org.springframework.http.HttpStatus;

import com.sc2006.g5.edufinder.exception.common.ApiException;

public class UserNotFoundException extends ApiException {
    public UserNotFoundException(Long id){
        super("User not found with id: %d".formatted(id), HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException(String username){
        super("User not found with id: %s".formatted(username), HttpStatus.NOT_FOUND);
    }
}
