package com.fintech.banking.model;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "payment")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Integer accountId;
    private String userId;
    private Integer accountNumber;
    private String accountName;
    private String accountType;
    private BigDecimal balance;
}
