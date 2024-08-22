package com.example.finalProject_synrgy.entity;

import com.example.finalProject_synrgy.entity.base.BaseDate;
import com.example.finalProject_synrgy.entity.enums.JenisRekening;
import com.example.finalProject_synrgy.entity.oauth2.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "rekening")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rekening extends BaseDate {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "card_number", unique = true)
    private Long cardNumber;

    @Column(name = "rekening_number", unique = true)
    private Long rekeningNumber;

    private String name;

    @Column(name = "jenis_rekening")
    @Enumerated(EnumType.STRING)
    private JenisRekening jenisRekening;

    @JsonIgnore
    @Column(name = "rekening_expired_date")
    private Date rekeningExpiredDate;

    @Column(name = "expired_date_month")
    private Integer expiredDateMonth;

    @Column(name = "expired_date_year")
    private Integer expiredDateYear;

    @JsonIgnore
    private String pin;

    private Integer balance;

    @JsonIgnore
    @ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "rekening", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "qris_id", referencedColumnName = "id")
    @JsonIgnore
    private Qris qris;
}

