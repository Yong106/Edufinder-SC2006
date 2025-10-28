package com.sc2006.g5.edufinder.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sc2006.g5.edufinder.exception.common.ApiException;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUnexpected(Exception e) {
        Map<String, Object> body = Map.of(
            "timestamp", LocalDateTime.now(),
            "status", 500,
            "error", "Internal Server Error",
            "message", e.getMessage()
        );
        e.printStackTrace();
        return ResponseEntity.status(500).body(body);
    }
}
