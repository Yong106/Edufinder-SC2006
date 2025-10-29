package com.sc2006.g5.edufinder.dto.api;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse<R extends ApiRecord> {
    private boolean success;
    private ApiResult<R> result;
}
