package com.example.finalProject_synrgy.repository;

import com.example.finalProject_synrgy.entity.Rekening;
import com.google.api.client.util.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface RekeningRepository extends JpaRepository<Rekening, UUID>, JpaSpecificationExecutor<Rekening> {
    Rekening findByCardNumber(Integer cardNumber);

    Rekening findByPin(Integer pin);

    Rekening findByBalance(Integer balance);

    Rekening findByJenisRekening(String jenisRekening);

    Rekening findByRekeningActiveDate(DateTime rekeningActiveDate);

    Rekening findByRekeningExpiredDate(DateTime rekeningExpiredDate);
}
