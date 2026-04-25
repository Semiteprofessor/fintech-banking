package com.fintech.banking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_reference", columnList = "reference"),
        @Index(name = "idx_created_at", columnList = "createdAt")
})
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String transactionId;

    // 🔥 Prevent infinite JSON loop
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    @JsonIgnore
    private Account account;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(length = 50)
    private String channel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(length = 10)
    private String reasonCode;

    @Column(nullable = false, length = 100)
    private String reference;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balanceAfter;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}