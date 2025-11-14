package com.sc2006.g5.edufinder.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Represents the response payload for list of schools returned by the API.
 */
@Data
@Builder
public class SchoolsResponse {

    private List<SchoolResponse> schools;
}
