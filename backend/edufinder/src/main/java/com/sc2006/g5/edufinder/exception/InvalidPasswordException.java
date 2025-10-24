package com.sc2006.g5.edufinder.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(){
        super("Password too weak. Please try again.");
    }
}
