package com.example.finalProject_synrgy.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

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

    @Range(min = 1000000000000000L, max = 9999999999999999L, message = "must in valid range (1000000000000000 - 9999999999999999) (16 digit)")
    private Long cardNumber;

    @NotEmpty(message = "must not empty")
    @Size(min = 13, max = 13, message = "must be 13 number")
    private String phoneNumber;

    @NotEmpty(message = "must not empty")
    @Size(min = 6, max = 6, message = "must be 6 number")
    private String pin;
}
