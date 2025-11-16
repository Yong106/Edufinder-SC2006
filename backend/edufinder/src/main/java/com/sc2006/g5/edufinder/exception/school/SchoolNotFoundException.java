package com.sc2006.g5.edufinder.exception.school;

import org.springframework.http.HttpStatus;

import com.sc2006.g5.edufinder.exception.common.ApiException;

/**
 * Thrown when an operation references a school that does not exist.
 * <p>
 * Returns HTTP 404 Not Found.
 */
public class SchoolNotFoundException extends ApiException {

    /**
     * Creates a new exception indicating that the school with the specified ID could not be found.
     *
     * @param schoolId the ID of the missing school
     */
    public SchoolNotFoundException(Long schoolId){
        super("School not found with id: %d".formatted(schoolId), HttpStatus.NOT_FOUND);
    }
}
