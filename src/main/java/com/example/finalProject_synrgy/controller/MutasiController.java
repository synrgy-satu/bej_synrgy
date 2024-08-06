package com.example.finalProject_synrgy.controller;

import com.example.finalProject_synrgy.dto.base.BaseResponse;
import com.example.finalProject_synrgy.dto.mutasi.MutasiResponse;
import com.example.finalProject_synrgy.entity.enums.JenisTransaksi;
import com.example.finalProject_synrgy.service.MutasiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Mutasi")
@RestController
@RequestMapping("v1/mutasi")
public class MutasiController {

    @Autowired
    MutasiService mutasiService;

//    @Operation(summary = "Lihat Mutasi Rekening di Mobile")
//    @GetMapping()
//    public ResponseEntity<?> readMutasiMobile(
//            @RequestParam String cardNumber,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
//            @RequestParam(required = false) String timePeriod,
//            @RequestParam(required = false) JenisTransaksi transactionType) {
//
//        return ResponseEntity.ok(BaseResponse.success(
//                mutasiService.read(cardNumber, startDate, endDate, timePeriod, transactionType),
//                "Success Get Mutation Data"
//        ));
//    }

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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.failure(500, "Failed to fetch transaction data"));
        }
    }
}
