package com.fintech.banking.dto.request;

import com.fintech.banking.constants.BillType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BillPaymentRequest {

    private String accountId;
    private BigDecimal amount;
    private String provider;        // e.g. MTN
    private String customerRef;     // phone or meter number
    private BillType billType;      // AIRTIME / DATA / ELECTRICITY
}
