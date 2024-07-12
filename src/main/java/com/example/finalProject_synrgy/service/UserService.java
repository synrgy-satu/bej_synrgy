package com.example.finalProject_synrgy.service;

import com.example.finalProject_synrgy.dto.UserRequest;
import com.example.finalProject_synrgy.dto.UserResponse;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse create(UserRequest userRequest);

    List<UserResponse> findAll(Pageable pageable, String username, String emailAddress);

    UserResponse update(Principal principal, UserRequest request);

    UserResponse delete(UUID id);

    UserResponse findById(UUID id);
}
