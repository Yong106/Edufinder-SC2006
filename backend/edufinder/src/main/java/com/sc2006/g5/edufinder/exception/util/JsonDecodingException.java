package com.sc2006.g5.edufinder.exception.util;

import org.springframework.http.HttpStatus;

import com.sc2006.g5.edufinder.exception.common.ApiException;

public class JsonDecodingException extends ApiException{
    public JsonDecodingException(String message){
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
