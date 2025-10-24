package com.sc2006.g5.edufinder.exception;

public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException(){
        super("Wrong username or password. Please try again.");
    }
}
