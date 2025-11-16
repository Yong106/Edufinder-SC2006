package com.sc2006.g5.edufinder.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents the link section of a response from the <a href="https://data.gov.sg/">data.gov.sg</a> API.
 * <p>
 * Contains fields:
 * <ul>
 *     <li>{@code next} – the next pagination link for more records</li>
 * </ul>
 *
 * @see ApiResponse
 * @see ApiRecord
 */
@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiLink {
    private String next;
}
