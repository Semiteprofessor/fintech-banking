package com.fintech.banking.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponse {

    private String transactionId;

    private String accountId;

    private String transactionType; // DEBIT / CREDIT / TRANSFER

    private BigDecimal amount;

    private String source;

    private String status; // SUCCESS / FAILED / PENDING

    private String reasonCode;

    private LocalDateTime createdAt;
}