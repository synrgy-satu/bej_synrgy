package com.example.finalProject_synrgy.dto.auth;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequest {
    @NotEmpty(message = "must not empty")
    @Email
    private String emailAddress;
    @NotEmpty(message = "must not empty")
    private String password;
}
