package com.fintech.banking.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_history")
@Data
@Immutable
public class TransactionHistory {

    @Id
    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "transaction_type")
    private String transactionType;

    private BigDecimal amount;

    private String source;

    private String status;

    @Column(name = "reason_code")
    private String reasonCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}