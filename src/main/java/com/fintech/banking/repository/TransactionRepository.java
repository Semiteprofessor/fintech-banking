package com.fintech.banking.repository;

import com.fintech.banking.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    Page<Transaction> findByUserId(String userId, Pageable pageable);

    Page<Transaction> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    Page<Transaction> findByAccount_AccountIdOrderByCreatedAtDesc(String accountId, Pageable pageable);

    Optional<Transaction> findByReference(String reference);

    Page<Transaction> findTop10ByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    List<Transaction> findByAccount_AccountNumberOrderByCreatedAtDesc(String accountNumber);
}