package com.example.finalProject_synrgy.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotEmpty(message = "must not empty")
    private String username;
    @NotEmpty(message = "must not empty")
    @Email
    private String emailAddress;
    @NotEmpty(message = "must not empty")
    private String password;

    @NotEmpty(message = "must not empty")
    private String bankAccountNumber;

    @NotEmpty(message = "must not empty")
    private String phoneNumber;

    @NotEmpty(message = "must not empty")
    private String pin;
}
