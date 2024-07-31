package com.example.finalProject_synrgy.entity;

import com.example.finalProject_synrgy.entity.enums.JenisKelamin;
import com.example.finalProject_synrgy.entity.enums.StatusUser;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.Table;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.List;

@Entity
@Data
@Table(name= "user", schema= "public")

public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private long idCustomers;

    private String name;

    private String nik;

    private String address;

    private Gender gender;

    @Column(name = "father_user")
    private String fatherName;

    @Column(name = "mother_user")
    private String motherName;

    @Column(name = "phone_number")
    private String phoneNumber;

    private Double income;

    private String username;

    private String password;

    private String email;

    private String mpin;

    @Column(name = "status_user")
    private StatusUser statusUser;

    @Column(name = "delete_at")
    private Timestamp deletedAt;

    @Column(name = "created_at")
    @CurrentTimestamp
    private Timestamp createdAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private List<BankAccounts> bankAcc
}
