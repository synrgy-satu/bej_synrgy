package com.example.finalProject_synrgy.service;

import com.example.finalProject_synrgy.dto.mutasi.MutasiResponse;
import com.example.finalProject_synrgy.entity.Transaction;
import com.example.finalProject_synrgy.entity.enums.JenisTransaksi;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface MutasiService {
//    List<MutasiResponse> read(String cardNumber, LocalDate startDate, LocalDate endDate, String timePeriod, JenisTransaksi transactionType);

    List<MutasiResponse> getMutasi(Long cardNumber, int month, int year, JenisTransaksi jenisTransaksi, String periodeMutasi);
    int convertMonthNameToNumber(String monthName);
    int[] parsePeriodeMutasi(String periodeMutasi);
}

