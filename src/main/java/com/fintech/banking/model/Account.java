package com.fintech.banking.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "banking")
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Integer accountId;
    private String userId;
    private Integer accountNumber;
    private String accountName;
    private String accountType;
    private BigDecimal balance;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;

    public Integer getAccountId() {
        return accountId;
    }
}
