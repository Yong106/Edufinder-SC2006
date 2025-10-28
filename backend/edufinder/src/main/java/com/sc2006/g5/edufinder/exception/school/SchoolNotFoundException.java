package com.sc2006.g5.edufinder.exception.school;

import org.springframework.http.HttpStatus;

import com.sc2006.g5.edufinder.exception.common.ApiException;

public class SchoolNotFoundException extends ApiException {
    public SchoolNotFoundException(Long id){
        super("School not found with id: %d".formatted(id), HttpStatus.NOT_FOUND);
    }
}
