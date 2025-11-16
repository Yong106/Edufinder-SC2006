package com.sc2006.g5.edufinder.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contains data needed to edit school cut-off point.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditSchoolCutOffPointRequest {

    @NotNull
    @Min(4)
    @Max(32)
    private int minCutOffPoint;

    @NotNull
    @Min(4)
    @Max(32)
    private int maxCutOffPoint;

}
