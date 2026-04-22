package com.fintech.banking.controller;

import com.fintech.banking.model.Transaction;
import com.fintech.banking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Transaction>> getUserTransactions(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdAt").descending());

        return ResponseEntity.ok(
                transactionService.getUserTransactions(userId, pageable)
        );
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<Page<Transaction>> getAccountTransactions(
            @PathVariable String accountId,
            Pageable pageable,
            Authentication authentication) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                transactionService.getAccountTransactions(accountId, userId, pageable)
        );
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransaction(
            @PathVariable String transactionId,
            Authentication authentication) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                transactionService.getTransaction(transactionId, userId)
        );
    }
}