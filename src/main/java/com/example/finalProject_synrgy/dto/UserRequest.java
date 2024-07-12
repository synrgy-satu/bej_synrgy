package com.example.finalProject_synrgy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank(message = "Must not empty")
    private String username;
    @NotBlank(message = "Must not empty")
    private String emailAddress;
    @NotBlank(message = "Must not empty")
    private String password;
}
