package com.fintech.banking.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanRequest {
    private String accountId;
    private BigDecimal amount;
}
