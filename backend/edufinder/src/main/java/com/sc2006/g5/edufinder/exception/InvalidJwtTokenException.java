package com.sc2006.g5.edufinder.exception;

public class InvalidJwtTokenException extends RuntimeException {
    public InvalidJwtTokenException(String message){
        super(message);
    }
}
