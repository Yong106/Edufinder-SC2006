package com.sc2006.g5.edufinder.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditUserRequest {

    @NotNull
    @Pattern(regexp = "\\d{6}", message = "Postal code must be a 6-digit number")
    private String postalCode;
}
