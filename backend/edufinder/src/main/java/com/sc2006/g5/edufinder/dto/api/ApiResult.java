package com.sc2006.g5.edufinder.dto.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents the result section of a response from the <a href="https://data.gov.sg/">data.gov.sg</a> API.
 * <p>
 * Contains fields:
 * <ul>
 *     <li>{@code records} – the data records returned by the API</li>
 *     <li>{@code link} – the pagination links</li>
 *     <li>{@code total} – the total number of records</li>
 * </ul>
 *
 * @param <R> the type of {@link ApiRecord} contained in the response
 *
 * @see ApiResponse
 * @see ApiRecord
 * @see ApiLink
 */
@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResult<R extends ApiRecord> {
    private List<R> records;

    @JsonProperty("_links")
    private ApiLink link;

    private int total;
}
