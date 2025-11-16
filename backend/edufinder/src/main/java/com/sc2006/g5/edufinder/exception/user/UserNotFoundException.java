package com.sc2006.g5.edufinder.exception.user;

import org.springframework.http.HttpStatus;

import com.sc2006.g5.edufinder.exception.common.ApiException;

/**
 * Thrown when an operation references a user that does not exist.
 * <p>
 * Returns HTTP 404 Not Found.
 */
public class UserNotFoundException extends ApiException {

    /**
     * Creates a new exception indicating that the user with the specified ID could not be found.
     *
     * @param userId the ID of the missing user
     */
    public UserNotFoundException(Long userId) {
        super("User not found with id: %d".formatted(userId), HttpStatus.NOT_FOUND);
    }

    /**
     * Creates a new exception indicating that the user with the specified username could not be found.
     *
     * @param username the username of the missing user
     */
    public UserNotFoundException(String username){
        super("User not found with username: %s".formatted(username), HttpStatus.NOT_FOUND);
    }
}
