package com.fintech.banking.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {

    private String transactionId;

    private String accountId;

    private String transactionType;

    private BigDecimal amount;

    private String channel;

    private String source;

    private String reference;

    private BigDecimal balanceAfter;

    private String status;

    private String reasonCode;

    private LocalDateTime createdAt;
}