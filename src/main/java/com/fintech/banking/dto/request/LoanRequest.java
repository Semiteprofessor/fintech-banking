package com.fintech.banking.dto.request;

import com.fintech.banking.constants.LoanType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanRequest {
    private String accountId;
    private BigDecimal amount;
    private LoanType loanType;
}
