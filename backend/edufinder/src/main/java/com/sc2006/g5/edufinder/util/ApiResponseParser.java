package com.sc2006.g5.edufinder.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sc2006.g5.edufinder.dto.api.ApiRecord;
import com.sc2006.g5.edufinder.dto.api.ApiResponse;
import com.sc2006.g5.edufinder.exception.util.JsonDecodingException;

/**
 * A utility class that provide methods to parse JSON to {@link ApiResponse}.
 */
@Component
@RequiredArgsConstructor
public class ApiResponseParser {

    private final ObjectMapper objectMapper;

    /**
     * Parses a JSON string into an {@link ApiResponse} containing objects of the specified {@link ApiRecord} type.
     * <p>
     * This method uses Jackson's {@code ObjectMapper} to deserialize the given JSON into an
     * {@code ApiResponse<R>} object. The generic type {@code R} specifies the concrete implementation
     * of {@code ApiRecord} that the response should contain.
     *
     * @param json the JSON string to parse
     * @param apiRecordClass the class object representing the type of {@code ApiRecord} to deserialize into
     * @param <R> the type of {@code ApiRecord} contained within the {@code ApiResponse}
     *
     * @return an {@code ApiResponse} containing deserialized {@code ApiRecord} objects
     *
     * @throws JsonDecodingException if the JSON cannot be parsed due to invalid formatting or structure
     *
     * @see ApiResponse
     * @see ApiRecord
     */
    public <R extends ApiRecord> ApiResponse<R> parseApiRecord(String json, Class<R> apiRecordClass) {
        try {
            return objectMapper.readValue(
                json, 
                objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, apiRecordClass)
            );
        } catch (JsonProcessingException e) {
            throw new JsonDecodingException(e.getMessage());
        }
    }
}