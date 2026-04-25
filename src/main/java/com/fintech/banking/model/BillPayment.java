package com.fintech.banking.model;

import com.fintech.banking.constants.BillType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bill_payments")
@Data
public class BillPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String billPaymentId;

    @ManyToOne
    private Account account;

    private String customerReference; // phone number or meter number

    @Enumerated(EnumType.STRING)
    private BillType billType;

    private BigDecimal amount;

    private String provider; // MTN, GLO, AIRTEL, etc.

    private String status;

    private String reference;

    private LocalDateTime createdAt = LocalDateTime.now();
}
