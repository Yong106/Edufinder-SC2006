package com.sc2006.g5.edufinder.mapper;

import com.sc2006.g5.edufinder.dto.response.UserResponse;
import com.sc2006.g5.edufinder.model.user.User;
import org.springframework.stereotype.Component;

/**
 * Mapper for user-related model.
 * <p>
 * Provides method to map to {@link UserResponse} DTOs.
 */
@Component
public class UserMapper {

    /**
     * Converts a {@link User} entity into a {@link UserResponse}.
     *
     * @param user the user to map
     *
     * @return the mapped {@code UserResponse}
     *
     * @see User
     * @see UserResponse
     */
    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .username(user.getUsername())
            .role(user.getRole())
            .postalCode(user.getPostalCode())
            .build();
    }
}
