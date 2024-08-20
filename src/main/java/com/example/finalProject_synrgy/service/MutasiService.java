package com.example.finalProject_synrgy.service;

import com.example.finalProject_synrgy.dto.mutasi.MutasiResponse;
import com.example.finalProject_synrgy.dto.mutasi.SumberRekeningResponse;
import com.example.finalProject_synrgy.entity.Transaction;
import com.example.finalProject_synrgy.entity.enums.JenisTransaksi;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MutasiService {
    List<MutasiResponse> getMutasi(Long cardNumber, int month, int year, JenisTransaksi jenisTransaksi, String periodeMutasi);
    int convertMonthNameToNumber(String monthName);
    int[] parsePeriodeMutasi(String periodeMutasi);

    List<MutasiResponse> getMutasiHariIni(Long cardNumber, JenisTransaksi jenisTransaksi);

    List<MutasiResponse> getMutasiTujuhHariTerakhir(Long cardNumber, JenisTransaksi jenisTransaksi);

    List<MutasiResponse> getMutasiByTanggal(Long cardNumber, String tanggalMulai, String tanggalSelesai, JenisTransaksi jenisTransaksi);

    List<MutasiResponse> getMutasiMobile(Long cardNumber, LocalDateTime startDate, LocalDateTime endDate, JenisTransaksi jenisTransaksi);

    String verifyPin(String pin, Principal principal);

    MutasiResponse getMutasiMobileById(UUID transactionId);

    List<SumberRekeningResponse> getSumberRekening(Principal principal);
}

