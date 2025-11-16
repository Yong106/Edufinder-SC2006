package com.sc2006.g5.edufinder.exception.school;

import com.sc2006.g5.edufinder.exception.common.ApiException;
import org.springframework.http.HttpStatus;

/**
 * Thrown when an invalid cut-off point range is provided,
 * such as when the minimum cut-off point is greater than the maximum cut-off point.
 * <p>
 * Returns HTTP 400 Bad Request.
 */
public class CutOffPointException extends ApiException {

    /**
     * Creates a new exception indicating that the specified cut-off point range is invalid.
     *
     * @param minCutOffPoint the minimum cut-off point provided
     * @param maxCutOffPoint the maximum cut-off point provided
     */
    public CutOffPointException(int minCutOffPoint, int maxCutOffPoint) {
        super("Min cut-off point (%d) cannot be larger that max cut-off point (%d)."
            .formatted(minCutOffPoint, maxCutOffPoint), HttpStatus.BAD_REQUEST);
    }
}
