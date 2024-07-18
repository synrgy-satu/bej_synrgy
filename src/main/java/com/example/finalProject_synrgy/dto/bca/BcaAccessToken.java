package com.example.finalProject_synrgy.dto.bca;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BcaAccessToken {
    private String accessToken;
    private ZonedDateTime expired_date;
}
