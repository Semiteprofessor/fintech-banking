package com.fintech.banking.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceResponse {
    private String accountId;
    private String accountNumber;
    private BigDecimal balance;
}
