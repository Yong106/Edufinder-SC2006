package com.sc2006.g5.edufinder.exception.user;

import org.springframework.http.HttpStatus;

import com.sc2006.g5.edufinder.exception.common.ApiException;

public class UserNotSaveSchoolException extends ApiException{
    public UserNotSaveSchoolException(Long userId, Long schoolId){
        super("User (id: %d) didn't save school with id: %d".formatted(userId, schoolId), HttpStatus.CONFLICT);
    }
}
