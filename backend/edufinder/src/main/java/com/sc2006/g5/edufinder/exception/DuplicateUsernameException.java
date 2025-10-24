package com.sc2006.g5.edufinder.exception;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(){
        super("Username existed. Please try again with another username.");
    }
}
