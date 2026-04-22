package com.fintech.banking.controller;

import com.fintech.banking.dto.AccountResponse;
import com.fintech.banking.model.Account;
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

    @PostMapping
    public ResponseEntity<Account> createAccount(
            @RequestParam String userId,
            @RequestParam String accountName,
            @RequestParam String accountType) {

        return ResponseEntity.ok(
                accountService.createAccount(userId, accountName, accountType)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> getUserAccounts(
            @PathVariable String userId) {

        return ResponseEntity.ok(
                accountService.getUserAccounts(userId)
        );
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(
            @PathVariable String accountId) {

        return ResponseEntity.ok(
                accountService.getAccountById(accountId)
        );
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<?> getBalance(@PathVariable String accountId) {

        return ResponseEntity.ok(
                java.util.Map.of(
                        "accountId", accountId,
                        "balance", accountService.getAccountBalance(accountId)
                )
        );
    }
}