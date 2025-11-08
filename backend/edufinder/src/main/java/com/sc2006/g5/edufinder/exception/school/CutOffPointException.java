package com.sc2006.g5.edufinder.exception.school;

import com.sc2006.g5.edufinder.exception.common.ApiException;
import org.springframework.http.HttpStatus;

public class CutOffPointException extends ApiException {
    public CutOffPointException(int minCutOffPoint, int maxCutOffPoint) {
        super("Min cut-off point (%d) cannot be larger that max cut-off point (%d)."
            .formatted(minCutOffPoint, maxCutOffPoint), HttpStatus.BAD_REQUEST);
    }
}
