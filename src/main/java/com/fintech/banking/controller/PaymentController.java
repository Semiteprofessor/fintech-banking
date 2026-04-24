package com.fintech.banking.controller;

import com.fintech.banking.dto.request.DepositRequest;
import com.fintech.banking.dto.request.TransferRequest;
import com.fintech.banking.dto.request.WithdrawalRequest;
import com.fintech.banking.dto.response.BalanceResponse;
import com.fintech.banking.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request) {

        return ResponseEntity.ok(
                Map.of("message", paymentService.transfer(request))
        );
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateBeneficiary(
            @RequestParam String accountNumber) {

        return ResponseEntity.ok(
                paymentService.validateAccount(accountNumber)
        );
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody DepositRequest request) {

        return ResponseEntity.ok(
                paymentService.deposit(request)
        );
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody WithdrawalRequest request) {
        return ResponseEntity.ok(paymentService.withdraw(
                request.getAccountId(),
                request.getAmount()
        ));
    }

    @GetMapping("/history")
    public ResponseEntity<?> getTransactionHistory(
            @RequestParam String accountId) {

        return ResponseEntity.ok(
                paymentService.getTransactionHistory(accountId)
        );
    }
}