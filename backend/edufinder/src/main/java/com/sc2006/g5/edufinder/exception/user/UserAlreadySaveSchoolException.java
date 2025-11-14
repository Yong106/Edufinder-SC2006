package com.sc2006.g5.edufinder.exception.user;

import org.springframework.http.HttpStatus;

import com.sc2006.g5.edufinder.exception.common.ApiException;

/**
 * Thrown when a user attempts to save a school that has already been saved previously.
 * <p>
 * Returns HTTP 409 Conflict.
 */
public class UserAlreadySaveSchoolException extends ApiException{

    /**
     * Creates a new exception indicating that the specified user has already saved
     * the given school.
     *
     * @param userId   the ID of the user attempting to save the school
     * @param schoolId the ID of the school that is already saved
     */
    public UserAlreadySaveSchoolException(Long userId, Long schoolId){
        super("User (id: %d) already saved school with id: %d".formatted(userId, schoolId), HttpStatus.CONFLICT);
    }
}
