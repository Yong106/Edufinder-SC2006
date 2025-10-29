package com.sc2006.g5.edufinder.exception.user;

import org.springframework.http.HttpStatus;

import com.sc2006.g5.edufinder.exception.common.ApiException;

public class UserAlreadySaveSchoolException extends ApiException{
    public UserAlreadySaveSchoolException(Long userId, Long schoolId){
        super("User (id: %d) already saved school with id: %d".formatted(userId, schoolId), HttpStatus.CONFLICT);
    }
}
