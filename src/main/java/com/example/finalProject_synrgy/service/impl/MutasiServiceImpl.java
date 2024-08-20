package com.example.finalProject_synrgy.service.impl;

import com.example.finalProject_synrgy.dto.mutasi.MutasiResponse;
import com.example.finalProject_synrgy.dto.mutasi.SumberRekeningResponse;
import com.example.finalProject_synrgy.entity.Rekening;
import com.example.finalProject_synrgy.entity.Transaction;
import com.example.finalProject_synrgy.entity.Vendors;
import com.example.finalProject_synrgy.entity.enums.JenisTransaksi;
import com.example.finalProject_synrgy.entity.oauth2.User;
import com.example.finalProject_synrgy.repository.RekeningRepository;
import com.example.finalProject_synrgy.repository.TransactionRepository;
import com.example.finalProject_synrgy.service.AuthService;
import com.example.finalProject_synrgy.service.MutasiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MutasiServiceImpl implements MutasiService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    private RekeningRepository rekeningRepository;

    @Autowired
    private AuthService authService;

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

        List<MutasiResponse> mutasiResponses = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        for (Transaction transaction : transactions) {
            MutasiResponse response = new MutasiResponse(
                    rekening.getUser().getUsername(),
                    cardNumber,
                    rekening.getJenisRekening(),
                    periodeMutasi,
                    transaction.getBalanceHistory(),
                    dateFormat.format(transaction.getCreated_date()),
                    transaction.getAmount(),
                    transaction.getReferenceNumber(),
                    transaction.getNote(),
                    transaction.getVendors() != null ? transaction.getVendors().getVendorCode() : null,
                    transaction.getVendors() != null ? transaction.getVendors().getVendorName() : null,
                    transaction.getJenisTransaksi()
            );

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

    @Override
    public List<MutasiResponse> getMutasiHariIni(Long cardNumber, JenisTransaksi jenisTransaksi) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now();
        return getMutasiMobile(cardNumber, startOfDay, endOfDay, jenisTransaksi);
    }

    @Override
    public List<MutasiResponse> getMutasiTujuhHariTerakhir(Long cardNumber, JenisTransaksi jenisTransaksi) {
        LocalDateTime tujuhHariLalu = LocalDateTime.now().minusDays(7);
        LocalDateTime now = LocalDateTime.now();
        return getMutasiMobile(cardNumber, tujuhHariLalu, now, jenisTransaksi);
    }

    @Override
    public List<MutasiResponse> getMutasiByTanggal(Long cardNumber, String tanggalMulai, String tanggalSelesai, JenisTransaksi jenisTransaksi) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime startDate = LocalDate.parse(tanggalMulai, formatter).atStartOfDay();
        LocalDateTime endDate = LocalDate.parse(tanggalSelesai, formatter).atTime(LocalTime.MAX);

        if (ChronoUnit.DAYS.between(startDate, endDate) > 90) {
            throw new IllegalArgumentException("Date range should not exceed 90 days.");
        }

        return getMutasiMobile(cardNumber, startDate, endDate, jenisTransaksi);
    }

    @Override
    public List<MutasiResponse> getMutasiMobile(Long cardNumber, LocalDateTime startDate, LocalDateTime endDate, JenisTransaksi jenisTransaksi) {
        Rekening rekening = rekeningRepository.findByCardNumber(cardNumber);

        if (rekening == null) {
            throw new IllegalArgumentException("Card number not found in the database.");
        }

        Date startDateConverted = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
        Date endDateConverted = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());

        String periodeMutasi = startDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + " to " + endDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        List<Transaction> transactions;
        if (jenisTransaksi == null || jenisTransaksi == JenisTransaksi.SEMUA) {
            transactions = transactionRepository.findByRekeningAndCreated_dateBetween(
                    rekening, startDateConverted, endDateConverted, Sort.by(Sort.Direction.DESC, "created_date"));
        } else {
            transactions = transactionRepository.findByRekeningAndCreated_dateBetweenAndJenisTransaksi(
                    rekening, startDateConverted, endDateConverted, jenisTransaksi, Sort.by(Sort.Direction.DESC, "created_date"));
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        return transactions.stream()
                .map(transaction -> new MutasiResponse(
                        rekening.getUser().getUsername(),
                        cardNumber,
                        rekening.getJenisRekening(),
                        periodeMutasi,
                        transaction.getBalanceHistory(),
                        dateFormat.format(transaction.getCreated_date()),
                        transaction.getAmount(),
                        transaction.getReferenceNumber(),
                        transaction.getNote(),
                        transaction.getVendors() != null ? transaction.getVendors().getVendorCode() : null,
                        transaction.getVendors() != null ? transaction.getVendors().getVendorName() : null,
                        transaction.getJenisTransaksi()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public String verifyPin(String pin, Principal principal) {
        User currentUser = authService.getCurrentUser(principal);

        if (currentUser.getPin() == null || !currentUser.getPin().equals(pin)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "PIN tidak valid.");
        }

        return "Verifikasi PIN berhasil.";
    }

    @Override
    public MutasiResponse getMutasiMobileById(UUID transactionId) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);

        if (!transactionOptional.isPresent()) {
            return null;
        }

        Transaction transaction = transactionOptional.get();

        Rekening rekening = transaction.getRekening();
        Vendors vendor = transaction.getVendors();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        MutasiResponse mutasiResponse = new MutasiResponse();
        mutasiResponse.setUsername(rekening.getUser().getUsername());
        mutasiResponse.setCardNumber(rekening.getCardNumber());
        mutasiResponse.setJenisRekening(rekening.getJenisRekening());
        mutasiResponse.setPeriodeMutasi("");
        mutasiResponse.setBalance(transaction.getBalanceHistory());
        mutasiResponse.setCreatedDate(dateFormat.format(transaction.getCreated_date()));
        mutasiResponse.setAmount(transaction.getAmount());
        mutasiResponse.setReferenceNumber(transaction.getReferenceNumber());
        mutasiResponse.setNote(transaction.getNote());
        mutasiResponse.setVendorCode(vendor != null ? vendor.getVendorCode() : null);
        mutasiResponse.setVendorName(vendor != null ? vendor.getVendorName() : null);
        mutasiResponse.setJenisTransaksi(transaction.getJenisTransaksi());

        return mutasiResponse;
    }

    @Override
    public List<SumberRekeningResponse> getSumberRekening(Principal principal) {
        User currentUser = authService.getCurrentUser(principal);
        List<Rekening> rekeningList = rekeningRepository.findByUserId(currentUser.getId());

        return rekeningList.stream()
                .map(rekening -> new SumberRekeningResponse(
                        currentUser.getFullName(),
                        rekening.getCardNumber(),
                        rekening.getJenisRekening()
                ))
                .collect(Collectors.toList());
    }
}
