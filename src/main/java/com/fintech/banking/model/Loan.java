package com.fintech.banking.model;

import com.fintech.banking.model.Account;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
@Data
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String loanId;

    @ManyToOne
    private Account account;

    private BigDecimal amount;
    private BigDecimal interestRate;
    private BigDecimal totalRepayment;

    private String status; // PENDING, APPROVED, REJECTED, PAID

    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
}