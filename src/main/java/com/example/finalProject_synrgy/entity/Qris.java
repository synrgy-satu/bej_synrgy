package com.example.finalProject_synrgy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "qris")
@Data
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

    private String name;
}
