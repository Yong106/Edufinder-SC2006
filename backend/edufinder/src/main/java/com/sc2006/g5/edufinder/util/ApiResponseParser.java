package com.sc2006.g5.edufinder.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sc2006.g5.edufinder.dto.api.ApiRecord;
import com.sc2006.g5.edufinder.dto.api.ApiResponse;
import com.sc2006.g5.edufinder.exception.util.JsonDecodingException;

@Component
public class ApiResponseParser {

    private final ObjectMapper objectMapper;

    @Autowired
    public ApiResponseParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

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