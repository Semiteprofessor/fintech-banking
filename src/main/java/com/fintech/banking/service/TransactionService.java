package com.fintech.banking.service;

import com.fintech.banking.model.Account;
import com.fintech.banking.model.Transaction;
import com.fintech.banking.repository.AccountRepository;
import com.fintech.banking.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public Page<Transaction> getUserTransactions(String userId, Pageable pageable) {
        return transactionRepository.findByUserId(userId, pageable);
    }

    public Page<Transaction> getAccountTransactions(String accountId, String userId, Pageable pageable) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        return transactionRepository.findByAccount_AccountIdOrderByCreatedAtDesc(accountId, pageable);
    }

    public Transaction getTransaction(String transactionId, String userId) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        return transaction;
    }

    public Transaction getByReference(String reference, String userId) {

        Transaction transaction = transactionRepository.findByReference(reference)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        return transaction;
    }
}