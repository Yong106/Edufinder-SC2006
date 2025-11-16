package com.sc2006.g5.edufinder.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Represents the response payload for saved schools returned by the API.
 * <p>
 * Contains list of school id.
 */
@Builder
@Data
@AllArgsConstructor
public class SavedSchoolResponse {
    private List<Long> savedSchoolIds;
}
