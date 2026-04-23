package com.fintech.banking.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountResponse {

    private String accountId;

    private String accountNumber;
    private String accountName;

    private String accountType;

    private BigDecimal balance;

    private String currency;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}