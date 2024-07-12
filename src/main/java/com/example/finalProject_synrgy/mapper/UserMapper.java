package com.example.finalProject_synrgy.mapper;


import com.example.finalProject_synrgy.dto.UserResponse;
import com.example.finalProject_synrgy.entity.oauth2.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .emailAddress(user.getEmailAddress())
                .build();
    }
}
