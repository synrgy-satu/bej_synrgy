package com.example.finalProject_synrgy.repository;

import com.example.finalProject_synrgy.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {

    Transaction findByAmount(Integer amount);

    Transaction findByJenisTransaksi(String jenisTransaksi);

}
