package com.example.finalProject_synrgy.dto.mutasi;

import com.example.finalProject_synrgy.entity.enums.JenisRekening;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@Data
public class MutasiResponse {
    private String username;
    private Long cardNumber;
    private JenisRekening jenisRekening;
    private String periodeMutasi;
    private Integer balance;
    private String createdDate;
    private Integer amount;
    private String referenceNumber;
    private String note;
    private String vendorCode;
    private String vendorName;


    public MutasiResponse(String username, Long cardNumber, JenisRekening jenisRekening, String periodeMutasi,
                          Integer balance, String createdDate, Integer amount, String referenceNumber, String note, String vendorCode, String vendorName) {
        this.username = username;
        this.cardNumber = cardNumber;
        this.jenisRekening = jenisRekening;
        this.periodeMutasi = periodeMutasi;
        this.balance = balance;
        this.createdDate = createdDate;
        this.amount = amount;
        this.referenceNumber = referenceNumber;
        this.note = note;
        this.vendorCode = vendorCode;
        this.vendorName = vendorName;
    }

    public void setPeriodeMutasi(String periodeMutasi) {
        this.periodeMutasi = periodeMutasi;
    }
}
