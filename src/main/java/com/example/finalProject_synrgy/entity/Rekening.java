package com.example.finalProject_synrgy.entity;

import com.example.finalProject_synrgy.entity.base.BaseDate;
import com.example.finalProject_synrgy.entity.enums.JenisRekening;
import com.example.finalProject_synrgy.entity.enums.JenisTransaksi;
import com.example.finalProject_synrgy.entity.oauth2.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.api.client.util.DateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

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

    @Column(name = "card_number")
    private Long cardNumber;

    @Column(name = "jenis_rekening")
    @Enumerated(EnumType.STRING)
    private JenisRekening jenisRekening;

//    @Column(name = "jenis_rekening")
//    private String jenisRekening;

//    @Column(name = "rekening_active_date")
//    private DateTime rekeningActiveDate;

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
}

