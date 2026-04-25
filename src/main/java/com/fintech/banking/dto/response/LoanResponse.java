package com.fintech.banking.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class LoanResponse {

    private String loanId;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private BigDecimal totalRepayment;
    private String status;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
}
