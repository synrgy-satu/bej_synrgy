package com.example.finalProject_synrgy.dto.infosaldo;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class InfoSaldoRequest {
    @NotNull(message = "customerId cannot be null")
    private Long customerId;
}
