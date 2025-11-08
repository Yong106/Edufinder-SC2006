package com.sc2006.g5.edufinder.dto.request;

import com.sc2006.g5.edufinder.model.user.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditUserRoleRequest {

    @NotNull
    private Role role;

}
