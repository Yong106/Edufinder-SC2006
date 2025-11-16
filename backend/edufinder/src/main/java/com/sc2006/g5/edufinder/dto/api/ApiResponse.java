package com.sc2006.g5.edufinder.dto.api;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents a generic response structure returned by the <a href="https://data.gov.sg/">data.gov.sg</a> API.
 * <p>
 * Contains fields:
 * <ul>
 *     <li>{@code success} – indicates whether the API request was successful</li>
 *     <li>{@code result} – the payload containing the request's result</li>
 * </ul>
 *
 * @param <R> the type of {@link ApiRecord} contained within the {@link ApiResult}
 *
 * @see ApiRecord
 * @see ApiResult
 */
@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse<R extends ApiRecord> {
    private boolean success;
    private ApiResult<R> result;
}
