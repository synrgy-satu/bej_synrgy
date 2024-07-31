package com.example.finalProject_synrgy.dto.rekening;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class RekeningCheckRequest {
    @Range(min = 1000000000000000L, max = 9999999999999999L, message = "must in valid range (1000000000000000 - 9999999999999999) (16 digit)")
    private Long cardNumber;

    @Range(min = 1, max = 12, message = "must in valid range (1-12)")
    private int month;

    @Range(min = 2000, max = 3000, message = "must in valid range (2000 - 3000)")
    private int year;
}
