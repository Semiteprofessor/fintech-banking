package com.fintech.banking.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositRequest {

    @NotBlank
    private String accountId;

    @NotNull
    @DecimalMin("1.0")
    private BigDecimal amount;

    private String reference;
}
