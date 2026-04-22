package com.fintech.banking.controller;

import com.fintech.banking.model.Transaction;
import com.fintech.banking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getUserTransactions(
            @PathVariable String userId) {

        return ResponseEntity.ok(
                transactionService.getUserTransactions(userId)
        );
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getAccountTransactions(
            @PathVariable String accountId) {

        return ResponseEntity.ok(
                transactionService.getAccountTransactions(accountId)
        );
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransaction(
            @PathVariable String transactionId) {

        return ResponseEntity.ok(
                transactionService.getTransaction(transactionId)
        );
    }
}