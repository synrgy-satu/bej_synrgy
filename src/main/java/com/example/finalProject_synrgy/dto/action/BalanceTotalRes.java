package com.example.finalProject_synrgy.dto.action;

import lombok.Data;

@Data
public class BalanceTotalRes {
    private Long balanceTotal = 0L;
    private String name;
    private Integer cardTotal;
}
