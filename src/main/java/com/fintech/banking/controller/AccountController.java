package com.fintech.banking.controller;

import com.fintech.banking.dto.AccountResponse;
import com.fintech.banking.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // =========================
    // CREATE ACCOUNT
    // =========================
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @RequestParam String userId,
            @RequestParam String accountName,
            @RequestParam String accountType) {

        return ResponseEntity.ok(
                accountService.createAccount(userId, accountName, accountType)
        );
    }

    // =========================
    // GET USER ACCOUNTS
    // =========================
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountResponse>> getUserAccounts(
            @PathVariable String userId) {

        return ResponseEntity.ok(
                accountService.getUserAccounts(userId)
        );
    }

    // =========================
    // GET ACCOUNT BY ID
    // =========================
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(
            @PathVariable String accountId) {

        return ResponseEntity.ok(
                accountService.getAccountById(accountId)
        );
    }

    // =========================
    // GET ACCOUNT BALANCE
    // =========================
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<?> getBalance(@PathVariable String accountId) {

        return ResponseEntity.ok(
                java.util.Map.of(
                        "accountId", accountId,
                        "balance", accountService.getBalance(accountId)
                )
        );
    }
}