package com.example.finalProject_synrgy.entity;

import com.example.finalProject_synrgy.entity.base.BaseDate;
import com.example.finalProject_synrgy.entity.enums.JenisTransaksi;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "transaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_date is null")


//tes

public class Transaction extends BaseDate {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    private Integer amount;

    @Enumerated(EnumType.STRING)
    private JenisTransaksi jenisTransaksi;

    private Boolean isDebited;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Rekening rekening;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vendors_id")
    private Vendors vendors;
}
