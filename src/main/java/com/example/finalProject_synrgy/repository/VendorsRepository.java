package com.example.finalProject_synrgy.repository;

import com.example.finalProject_synrgy.entity.Vendors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface VendorsRepository extends JpaRepository<Vendors, UUID>, JpaSpecificationExecutor<Vendors> {
    Vendors findByVendorCode(String vendorCode);

    Vendors findByVendorName(String vendorName);
}
