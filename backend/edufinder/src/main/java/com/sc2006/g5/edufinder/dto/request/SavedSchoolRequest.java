package com.sc2006.g5.edufinder.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contains data needed to manage saved school.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavedSchoolRequest {

    @NotNull
    private Long schoolId;

}
