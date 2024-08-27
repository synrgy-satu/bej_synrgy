package com.example.finalProject_synrgy.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class EditPinDto {
    @NotEmpty(message = "must not empty")
    @Size(min = 6, max = 6, message = "must be 6 digits")
    private String newPin;

    @NotEmpty(message = "must not empty")
    @Size(min = 6, max = 6, message = "must be 6 digits")
    private String oldPin;
}
