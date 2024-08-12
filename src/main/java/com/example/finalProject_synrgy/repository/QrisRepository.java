package com.example.finalProject_synrgy.repository;

import com.example.finalProject_synrgy.entity.Qris;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface QrisRepository extends JpaRepository<Qris, UUID>, JpaSpecificationExecutor<Qris> {
    Qris findByEncodedQrCode(String encodedQrCode);
}
