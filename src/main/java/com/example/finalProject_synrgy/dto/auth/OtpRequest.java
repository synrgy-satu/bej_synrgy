package com.example.finalProject_synrgy.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpRequest {
    @NotEmpty(message = "Must not empty")
    private String otp;
}
