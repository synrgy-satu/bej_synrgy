package com.example.finalProject_synrgy.dto.infosaldo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class InfoSaldoRequest {
    @NotNull(message = "customerId cannot be null")
    private Long customerId;
}
