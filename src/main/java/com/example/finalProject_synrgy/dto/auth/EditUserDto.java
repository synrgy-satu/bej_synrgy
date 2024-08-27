package com.example.finalProject_synrgy.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class EditUserDto {
    @NotBlank(message = "Must not empty")
    private String fullName;
    @NotBlank(message = "Must not empty")
    private String emailAddress;
    @NotBlank(message = "Must not empty")
    private String phoneNumber;
}
