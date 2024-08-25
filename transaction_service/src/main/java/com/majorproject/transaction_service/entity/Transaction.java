package com.majorproject.transaction_service.entity;

import com.majorproject.jbdl_wallet_library.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "wallet_transactions")
public class Transaction {

    @Id
    @UuidGenerator
    private String transactionId ;

    @Column(nullable = false)
    private Long walletId ;

    @Column(nullable = false)
    private Long senderId ;

    @Column(nullable = false)
    private Long receiverId ;

    @Column(nullable = false)
    private Double amount ;

    private PaymentStatus paymentStatus ;


    @CreationTimestamp
    private LocalDateTime transactionInitiationDate ;

    @UpdateTimestamp
    private LocalDateTime transactionCompletionDate ;


    public Transaction(Long walletId, Long senderId, Long receiverId, Double amount) {
        this.walletId = walletId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
    }
}
