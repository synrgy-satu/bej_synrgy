package com.example.finalProject_synrgy.dto.mutasi;

import com.example.finalProject_synrgy.entity.enums.JenisRekening;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SumberRekeningResponse {
    private String fullName;
    private Long cardNumber;
    private JenisRekening jenisRekening;
}
