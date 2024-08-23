package com.majorproject.wallet_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "wallet")
@ToString
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletId ;

    @Column(nullable = false, unique = true)
    private Long userId ;

    private Double walletBalance ;

    private Integer dailyLimit ;

    private Integer dailyTransactionLimit ;

    @CreationTimestamp
    private LocalDateTime walletCreationTime ;

    public Wallet(Long userId) {
        this.userId = userId;
        this.walletBalance = 0.0;
        this.dailyTransactionLimit = 10 ;
        this.dailyLimit = 100000 ;
    }

    @UpdateTimestamp
    private LocalDateTime walletUpdationTime ;
}
