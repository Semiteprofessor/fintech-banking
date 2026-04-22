package com.fintech.banking.controller;

import com.fintech.banking.dto.DepositRequest;
import com.fintech.banking.dto.TransferRequest;
import com.fintech.banking.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request) {

        String response = paymentService.transfer(request);

        return ResponseEntity.ok(
                java.util.Map.of("message", response)
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
    public ResponseEntity<String> withdraw(@RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(paymentService.withdraw(
                request.getAccountId(),
                request.getAmount()
        ));
    }
}