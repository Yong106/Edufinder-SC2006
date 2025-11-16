package com.sc2006.g5.edufinder.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sc2006.g5.edufinder.exception.common.ApiException;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * A {@code RestControllerAdvice} class to handle {@link ApiException}.
 * All other exceptions are handled by default Spring Boot handler.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link ApiException} and return corresponding {@code ResponseEntity}.
     * The response body consists of timestamp, status, error, and message.
     *
     * @param e the exception to handles.
     * @return a {@code ResponseEntity} corresponding to the {@code ApiException}
     *
     * @see ApiException
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleApiException(ApiException e) {
        Map<String, Object> body = Map.of(
            "timestamp", LocalDateTime.now(),
            "status", e.getHttpStatus().value(),
            "error", e.getHttpStatus().getReasonPhrase(),
            "message", e.getMessage()
        );
        
        return ResponseEntity.status(e.getHttpStatus()).body(body);
    }

}
