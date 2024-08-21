package com.example.finalProject_synrgy.repository;

import com.example.finalProject_synrgy.entity.Rekening;
import com.example.finalProject_synrgy.entity.oauth2.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface RekeningRepository extends JpaRepository<Rekening, UUID>, JpaSpecificationExecutor<Rekening> {
    Rekening findByCardNumber(Long cardNumber);

    Rekening findByPin(Integer pin);

    Rekening findByBalance(Integer balance);

    Rekening findByJenisRekening(Enum jenisRekening);

//    Rekening findByRekeningActiveDate(DateTime rekeningActiveDate);

    Rekening findByRekeningExpiredDate(Date rekeningExpiredDate);

    boolean existsByCardNumber(Long cardNumber);

    List<Rekening> findAllByUser(User user);

    Rekening findByRekeningNumber(Long rekeningNumber);

    boolean existsByRekeningNumber(Long rekeningNumber);

    List<Rekening> findByUserId(UUID id);
}
