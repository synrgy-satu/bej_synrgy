package com.example.finalProject_synrgy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "qris")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Qris {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "encoded_qr_code")
    private String encodedQrCode;

    @JsonIgnore
    @OneToOne(mappedBy = "qris")
    private Rekening rekening;

    private String imageUrl;

    @Transient
    @JsonIgnore
    private String owner;

    private Long nmid;

    private Date activeUntil;

    private boolean isActive;

    private String name;
}
