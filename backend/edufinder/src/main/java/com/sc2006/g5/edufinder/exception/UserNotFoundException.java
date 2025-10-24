package com.sc2006.g5.edufinder.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(){
        super("User not found. Please try again");
    }
}
