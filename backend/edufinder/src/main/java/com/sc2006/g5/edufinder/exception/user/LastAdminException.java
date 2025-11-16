package com.sc2006.g5.edufinder.exception.user;

import com.sc2006.g5.edufinder.exception.common.ApiException;
import org.springframework.http.HttpStatus;

/**
 * Thrown when an operation attempts to demote the last remaining admin user.
 * <p>
 * Returns HTTP 409 Conflict.
 */
public class LastAdminException extends ApiException {

    /**
     * Creates a new exception indicating that the specified user cannot be demoted
     * because they are the last remaining admin.
     *
     * @param userId the ID of the admin user who cannot be demoted
     */
    public LastAdminException(Long userId) {
        super("User (id: %d) is the last admin. He/she cannot be demoted.".formatted(userId), HttpStatus.CONFLICT);
    }
}