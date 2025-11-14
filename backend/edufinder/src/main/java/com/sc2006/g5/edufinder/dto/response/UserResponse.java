package com.sc2006.g5.edufinder.dto.response;

import com.sc2006.g5.edufinder.model.user.Role;
import lombok.Builder;
import lombok.Data;

/**
 * Represents the response payload for a user returned by the API.
 */
@Data
@Builder
public class UserResponse {

    private Long id;
    private String username;
    private Role role;
    private String postalCode;
}
