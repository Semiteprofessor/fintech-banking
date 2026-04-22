package com.fintech.banking.model;

import com.fintech.banking.constants.TransactionStatus;
import com.fintech.banking.constants.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions", indexes = {
        @Index(name = "idx_account_id", columnList = "account_id"),
        @Index(name = "idx_user_id", columnList = "userId"),
        @Index(name = "idx_reference", columnList = "reference")
})
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    private BigDecimal amount;

    private String channel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    private String reasonCode;

    @Column(nullable = false, unique = true)
    private String reference;

    @Column(nullable = false)
    private BigDecimal balanceAfter;

    @CreationTimestamp
    private LocalDateTime createdAt;
}