package com.example.finalProject_synrgy.dto.infosaldo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfoSaldoResponse {
    private long customerId;
    private String username;
    private String cardNumber;
    private Amount amount;
}
