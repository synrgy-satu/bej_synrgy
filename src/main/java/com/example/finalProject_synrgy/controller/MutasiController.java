package com.example.finalProject_synrgy.controller;

import com.example.finalProject_synrgy.dto.base.BaseResponse;
import com.example.finalProject_synrgy.dto.mutasi.MutasiResponse;
import com.example.finalProject_synrgy.dto.mutasi.SumberRekeningResponse;
import com.example.finalProject_synrgy.entity.enums.JenisTransaksi;
import com.example.finalProject_synrgy.service.MutasiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Tag(name = "Mutasi")
@RestController
@RequestMapping("v1/mutasi")
public class MutasiController {

    @Autowired
    MutasiService mutasiService;

    @Operation(summary = "Lihat Mutasi Rekening di Web")
    @GetMapping()
    public ResponseEntity<BaseResponse<List<MutasiResponse>>> getMutasi(
            @RequestParam("cardNumber") Long cardNumber,
            @RequestParam("periodeMutasi") String periodeMutasi,
            @RequestParam("jenisTransaksi") JenisTransaksi jenisTransaksi) {

        try {
            int[] monthYear = mutasiService.parsePeriodeMutasi(periodeMutasi);

            List<MutasiResponse> transactions = mutasiService.getMutasi(cardNumber, monthYear[0], monthYear[1], jenisTransaksi, periodeMutasi);
            if (transactions.isEmpty()) {
                return ResponseEntity.ok(BaseResponse.success(transactions, "No mutasi data found for the given criteria."));
            }

            return ResponseEntity.ok(BaseResponse.success(transactions, "Success Read Mutasi"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(BaseResponse.failure(400, e.getMessage()));
        } catch (Exception e) {
//            System.err.println("Error fetching transaction data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.failure(500, "Failed to fetch transaction data"));
        }
    }

    @Operation(summary = "Lihat Mutasi Rekening di Mobile")
    @GetMapping("/mobile")
    public ResponseEntity<BaseResponse<List<MutasiResponse>>> getMutasiMobile(
            @RequestParam("cardNumber") Long cardNumber,
            @RequestParam(value = "periodeMutasi", required = false) String periodeMutasi,
            @RequestParam JenisTransaksi jenisTransaksi,
            @RequestParam(value = "tanggalMulai", required = false) String tanggalMulai,
            @RequestParam(value = "tanggalSelesai", required = false) String tanggalSelesai) {

        try {
            List<MutasiResponse> transactions;

            if ("hari ini".equalsIgnoreCase(periodeMutasi)) {
                LocalDateTime startDate = LocalDate.now().atStartOfDay();
                LocalDateTime endDate = LocalDate.now().atTime(LocalTime.MAX);
                transactions = mutasiService.getMutasiMobile(cardNumber, startDate, endDate, jenisTransaksi);
            } else if ("tujuh hari terakhir".equalsIgnoreCase(periodeMutasi)) {
                LocalDateTime endDate = LocalDate.now().atTime(LocalTime.MAX);
                LocalDateTime startDate = endDate.minusDays(7).toLocalDate().atStartOfDay();
                transactions = mutasiService.getMutasiMobile(cardNumber, startDate, endDate, jenisTransaksi);
            } else if (tanggalMulai != null && tanggalSelesai != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.forLanguageTag("id"));
                LocalDate startDate;
                LocalDate endDate;

                try {
                    startDate = LocalDate.parse(tanggalMulai, formatter);
                    endDate = LocalDate.parse(tanggalSelesai, formatter);
                } catch (DateTimeParseException e) {
                    return ResponseEntity.badRequest().body(BaseResponse.failure(400, "Tanggal tidak valid. Format yang diharapkan adalah 'dd MMMM yyyy'."));
                }

                if (endDate.isBefore(startDate)) {
                    return ResponseEntity.badRequest().body(BaseResponse.failure(400, "Tanggal selesai tidak boleh lebih kecil dari tanggal mulai."));
                }

                if (ChronoUnit.DAYS.between(startDate, endDate) > 90) {
                    return ResponseEntity.badRequest().body(BaseResponse.failure(400, "Rentang tanggal tidak boleh lebih dari 90 hari."));
                }

                LocalDateTime startDateTime = startDate.atStartOfDay();
                LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

                transactions = mutasiService.getMutasiMobile(cardNumber, startDateTime, endDateTime, jenisTransaksi);
            } else {
                int[] monthYear = mutasiService.parsePeriodeMutasi(periodeMutasi);
                LocalDate startLocalDate = LocalDate.of(monthYear[1], monthYear[0], 1);
                LocalDate endLocalDate = startLocalDate.withDayOfMonth(startLocalDate.lengthOfMonth());

                LocalDateTime startDate = startLocalDate.atStartOfDay();
                LocalDateTime endDate = endLocalDate.atTime(LocalTime.MAX);
                transactions = mutasiService.getMutasiMobile(cardNumber, startDate, endDate, jenisTransaksi);
            }

            if (transactions.isEmpty()) {
                return ResponseEntity.ok(BaseResponse.success(transactions, "No mutasi data found for the given criteria."));
            }

            return ResponseEntity.ok(BaseResponse.success(transactions, "Success Read Mutasi"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(BaseResponse.failure(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.failure(500, "Failed to fetch transaction data"));
        }
    }

    @PostMapping("/pin")
    public ResponseEntity<BaseResponse<String>> verifyPin(@RequestParam("pin") String pin, Principal principal) {
        try {
            String result = mutasiService.verifyPin(pin, principal);
            return ResponseEntity.ok(BaseResponse.success(result, "Verifikasi PIN Berhasil"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatus()).body(BaseResponse.failure(e.getStatus().value(), e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.failure(500, "Terjadi kesalahan saat verifikasi PIN."));
        }
    }

    @Operation(summary = "Lihat Data Mutasi Rekening Berdasarkan ID")
    @GetMapping("/mobile/{transactionId}")
    public ResponseEntity<BaseResponse<MutasiResponse>> getMutasiMobileById(
            @PathVariable("transactionId") UUID transactionId) {
        try {
            MutasiResponse mutasiResponse = mutasiService.getMutasiMobileById(transactionId);

            if (mutasiResponse == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponse.failure(404, "Mutasi tidak ditemukan."));
            }

            return ResponseEntity.ok(BaseResponse.success(mutasiResponse, "Success Read Mutasi By ID"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.failure(500, "Failed to fetch transaction data by ID"));
        }
    }

    @Operation(summary = "Lihat Data Sumber Rekening")
    @GetMapping("/sumber-rekening")
    public ResponseEntity<BaseResponse<List<SumberRekeningResponse>>> getSumberRekening(Principal principal) {
        try {
            List<SumberRekeningResponse> sumberRekening = mutasiService.getSumberRekening(principal);
            return ResponseEntity.ok(BaseResponse.success(sumberRekening, "Data sumber rekening berhasil diambil."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.failure(500, "Terjadi kesalahan saat mengambil data sumber rekening."));
        }
    }
}
