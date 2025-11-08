package com.sc2006.g5.edufinder.exception.user;

import com.sc2006.g5.edufinder.exception.common.ApiException;
import org.springframework.http.HttpStatus;

public class LastAdminException extends ApiException {
    public LastAdminException(Long userId) {
        super("User (id: %d) is the last admin. He/she cannot be demoted.".formatted(userId), HttpStatus.CONFLICT);
    }
}