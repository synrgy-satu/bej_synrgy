package com.example.finalProject_synrgy.repository;

import com.example.finalProject_synrgy.dto.mutasi.MutasiResponse;
import com.example.finalProject_synrgy.entity.Transaction;
import com.example.finalProject_synrgy.entity.enums.JenisTransaksi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {

    Transaction findByAmount(Integer amount);

    Transaction findByJenisTransaksi(Enum jenisTransaksi);

    @Query("SELECT new com.example.finalProject_synrgy.dto.mutasi.MutasiResponse(" +
            "u.username, r.cardNumber, r.jenisRekening, " +
            "CONCAT(FUNCTION('TO_CHAR', t.created_date, 'MM-YYYY'), ' ', :periodeMutasi), " +
            "r.balance, FUNCTION('TO_CHAR', t.created_date, 'DD-MM-YYYY'), " +
            "t.amount, v.vendorCode, v.vendorName) " +
            "FROM Transaction t " +
            "JOIN t.rekening r " +
            "JOIN r.user u " +
            "LEFT JOIN t.vendors v " +
            "WHERE r.cardNumber = :cardNumber " +
            "AND EXTRACT(MONTH FROM t.created_date) = :month " +
            "AND EXTRACT(YEAR FROM t.created_date) = :year " +
            "AND (:jenisTransaksi = 'SEMUA' OR t.jenisTransaksi = :jenisTransaksi)")
    List<MutasiResponse> findMutasi(@Param("cardNumber") Long cardNumber,
                                    @Param("month") int month,
                                    @Param("year") int year,
                                    @Param("jenisTransaksi") String jenisTransaksi,
                                    @Param("periodeMutasi") String periodeMutasi);

}
