package com.example.finalProject_synrgy.entity;

import com.example.finalProject_synrgy.entity.base.BaseDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.api.client.util.DateTime;
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

//teslagi
public class Transaction extends BaseDate {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @JsonIgnore
    private Integer amount;

    @JsonIgnore
    private String jenisTransaksi;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rekening_id")
    private Rekening rekening;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vendors_id")
    private Vendors vendors;
}

