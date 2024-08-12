package com.example.finalProject_synrgy.service.impl;

import com.example.finalProject_synrgy.dto.mutasi.MutasiResponse;
import com.example.finalProject_synrgy.entity.Rekening;
import com.example.finalProject_synrgy.entity.Transaction;
import com.example.finalProject_synrgy.entity.enums.JenisTransaksi;
import com.example.finalProject_synrgy.repository.RekeningRepository;
import com.example.finalProject_synrgy.repository.TransactionRepository;
import com.example.finalProject_synrgy.service.MutasiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MutasiServiceImpl implements MutasiService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    private RekeningRepository rekeningRepository;

//    @Override
//    public List<MutasiResponse> read(String cardNumber, LocalDate startDate, LocalDate endDate, String timePeriod, JenisTransaksi transactionType) {
//        if (timePeriod != null) {
//            switch (timePeriod) {
//                case "today":
//                    startDate = LocalDate.now();
//                    endDate = LocalDate.now();
//                    break;
//                case "last7days":
//                    startDate = LocalDate.now().minusDays(7);
//                    endDate = LocalDate.now();
//                    break;
//                case "last30days":
//                    startDate = LocalDate.now().minusDays(30);
//                    endDate = LocalDate.now();
//                    break;
//            }
//        }
//
//        return mutasiRepository.findByCriteria(cardNumber, startDate, endDate, transactionType);
//    }

    @Override
    public List<MutasiResponse> getMutasi(Long cardNumber, int month, int year, JenisTransaksi jenisTransaksi, String periodeMutasi) {
        Rekening rekening = rekeningRepository.findByCardNumber(cardNumber);

        if (rekening == null) {
            throw new IllegalArgumentException("Card number not found in the database.");
        }

        LocalDateTime startDate = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(year, month, YearMonth.of(year, month).lengthOfMonth()).atTime(LocalTime.MAX);

        Date startDateConverted = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
        Date endDateConverted = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());

        List<Transaction> transactions;
        if (jenisTransaksi == JenisTransaksi.SEMUA) {
            transactions = transactionRepository.findByRekeningAndCreated_dateBetween(
                    rekening, startDateConverted, endDateConverted, Sort.by(Sort.Direction.DESC, "created_date"));
        } else {
            transactions = transactionRepository.findByRekeningAndCreated_dateBetweenAndJenisTransaksi(
                    rekening, startDateConverted, endDateConverted, jenisTransaksi, Sort.by(Sort.Direction.DESC, "created_date"));
        }

        Integer saldoAwal = rekening.getBalance();
        List<MutasiResponse> mutasiResponses = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        for (Transaction transaction : transactions) {
            MutasiResponse response = new MutasiResponse(
                    rekening.getUser().getUsername(),
                    cardNumber,
                    rekening.getJenisRekening(),
                    periodeMutasi,
                    saldoAwal,
                    dateFormat.format(transaction.getCreated_date()),
                    transaction.getAmount(),
                    transaction.getReferenceNumber(),
                    transaction.getNote(),
                    transaction.getVendors() != null ? transaction.getVendors().getVendorCode() : null,
                    transaction.getVendors() != null ? transaction.getVendors().getVendorName() : null
            );

            if (transaction.getJenisTransaksi() == JenisTransaksi.TRANSAKSI_MASUK) {
                saldoAwal -= transaction.getAmount();
            } else if (transaction.getJenisTransaksi() == JenisTransaksi.TRANSAKSI_KELUAR) {
                saldoAwal += transaction.getAmount();
            }

            mutasiResponses.add(response);
        }

        return mutasiResponses;
    }



    @Override
    public int convertMonthNameToNumber(String monthName) {
        Map<String, Integer> monthMap = new HashMap<>();
        monthMap.put("Januari", 1);
        monthMap.put("Februari", 2);
        monthMap.put("Maret", 3);
        monthMap.put("April", 4);
        monthMap.put("Mei", 5);
        monthMap.put("Juni", 6);
        monthMap.put("Juli", 7);
        monthMap.put("Agustus", 8);
        monthMap.put("September", 9);
        monthMap.put("Oktober", 10);
        monthMap.put("November", 11);
        monthMap.put("Desember", 12);

        Integer month = monthMap.get(monthName);
        if (month == null) {
            throw new IllegalArgumentException("Invalid month name: " + monthName + " please double check and the first letter of the month is uppercase");
        }
        return month;
    }

    @Override
    public int[] parsePeriodeMutasi(String periodeMutasi) {
        String[] periodeParts = periodeMutasi.split(" ");
        if (periodeParts.length != 2) {
            throw new IllegalArgumentException("Invalid periodeMutasi format. Expected format: 'MM yyyy'");
        }

        int month = convertMonthNameToNumber(periodeParts[0]);
        int year = Integer.parseInt(periodeParts[1]);

        return new int[]{month, year};
    }
}
