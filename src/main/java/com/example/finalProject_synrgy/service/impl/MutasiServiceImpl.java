package com.example.finalProject_synrgy.service.impl;

import com.example.finalProject_synrgy.dto.mutasi.MutasiResponse;
import com.example.finalProject_synrgy.entity.enums.JenisTransaksi;
import com.example.finalProject_synrgy.repository.RekeningRepository;
import com.example.finalProject_synrgy.repository.TransactionRepository;
import com.example.finalProject_synrgy.service.MutasiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        if (!rekeningRepository.existsByCardNumber(cardNumber)) {
            throw new IllegalArgumentException("Card number not found in the database.");
        }

        String jenisTransaksiStr = jenisTransaksi.name();
        List<MutasiResponse> mutasiResponses = transactionRepository.findMutasi(cardNumber, month, year, jenisTransaksiStr, periodeMutasi);

        for (MutasiResponse response : mutasiResponses) {
            response.setPeriodeMutasi(periodeMutasi);
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
