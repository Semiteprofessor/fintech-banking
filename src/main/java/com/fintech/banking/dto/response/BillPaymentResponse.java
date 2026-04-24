package com.fintech.banking.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class BillPaymentResponse {

    private String billPaymentId;
    private String billType;        // AIRTIME, DATA, ELECTRICITY
    private String provider;        // MTN, GLO, AIRTEL
    private String customerReference; // phone or meter number
    private BigDecimal amount;
    private String status;          // SUCCESS / FAILED
    private String reference;
    private LocalDateTime createdAt;
}
