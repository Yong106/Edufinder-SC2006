package com.sc2006.g5.edufinder.mapper;

import com.sc2006.g5.edufinder.dto.response.UserResponse;
import com.sc2006.g5.edufinder.model.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .username(user.getUsername())
            .role(user.getRole())
            .postalCode(user.getPostalCode())
            .build();
    }
}
