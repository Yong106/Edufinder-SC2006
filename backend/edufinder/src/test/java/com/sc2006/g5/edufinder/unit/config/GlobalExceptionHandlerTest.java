package com.sc2006.g5.edufinder.unit.config;

import com.sc2006.g5.edufinder.config.GlobalExceptionHandler;
import com.sc2006.g5.edufinder.exception.common.ApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {

    GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Nested
    @DisplayName("handleApiException()")
    class handleApiExceptionTest{

        private static final HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;
        private static final String NOT_FOUND_MESSAGE = "not found";

        @Test
        @DisplayName("should return response entity with correct format")
        public void shouldReturnResponseEntityWithCorrectFormat() {
            ApiException notFoundException = new ApiException(NOT_FOUND_MESSAGE, NOT_FOUND);
            ResponseEntity<Map<String, Object>> responseEntity = globalExceptionHandler.handleApiException(notFoundException);

            assertNotNull(responseEntity.getBody());

            assertEquals(NOT_FOUND_MESSAGE, responseEntity.getBody().get("message"));
            assertEquals(NOT_FOUND.value(), responseEntity.getBody().get("status"));
            assertEquals(NOT_FOUND.getReasonPhrase(), responseEntity.getBody().get("error"));
            assertNotNull(responseEntity.getBody().get("timestamp"));
        }
    }
}
