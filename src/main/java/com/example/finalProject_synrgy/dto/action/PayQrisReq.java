package com.example.finalProject_synrgy.dto.action;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class PayQrisReq {
    @Range(min = 1000000000L, max = 9999999999L, message = "must in valid range (1000000000 - 9999999999) (10 digit)")
    private Long debitedRekeningNumber;

    @NotEmpty(message = "must not empty")
    private String targetQris;

    @Range(min = 1, message = "must in valid range (amount > 0)")
    private int amount;

    @Size(min = 6, max = 6, message = "must be 6 number")
    private String pin;
}
