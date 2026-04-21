package com.fintech.banking.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments_history")
@Data
@Immutable
public class PaymentHistory {

    @Id
    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "user_id")
    private String userId;

    private String beneficiary;

    @Column(name = "beneficiary_acc_no")
    private String beneficiaryAccNo;

    private BigDecimal amount;

    private String status;

    @Column(name = "reference_no")
    private String referenceNo;

    @Column(name = "reason_code")
    private String reasonCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}