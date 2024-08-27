package com.example.finalProject_synrgy.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class EditPasswordDto {
    @NotEmpty(message = "must not empty")
    private String newPassword;

    @NotEmpty(message = "must not empty")
    private String oldPassword;
}
