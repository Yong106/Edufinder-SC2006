package com.sc2006.g5.edufinder.exception.user;

import org.springframework.http.HttpStatus;

import com.sc2006.g5.edufinder.exception.common.ApiException;

/**
 * Thrown when a user attempts to remove with a saved school entry
 * that does not exist for that user.
 * <p>
 * Returns HTTP 409 Conflict.
 */
public class UserNotSaveSchoolException extends ApiException{

    /**
     * Creates a new exception indicating that the specified user has not saved the given school.
     *
     * @param userId   the ID of the user attempting to remove the saved school
     * @param schoolId the ID of the school that has not been saved by the user
     */
    public UserNotSaveSchoolException(Long userId, Long schoolId){
        super("User (id: %d) didn't save school with id: %d".formatted(userId, schoolId), HttpStatus.CONFLICT);
    }
}
