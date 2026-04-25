package com.fintech.banking.controller;

import com.fintech.banking.dto.request.LoanRequest;
import com.fintech.banking.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/apply")
    public ResponseEntity<?> apply(@RequestBody LoanRequest request) {
        return ResponseEntity.ok(loanService.applyLoan(request));
    }

    @PostMapping("/approve/{loanId}")
    public ResponseEntity<?> approve(@PathVariable String loanId) {
        return ResponseEntity.ok(loanService.approveLoan(loanId));
    }

    @PostMapping("/repay")
    public ResponseEntity<?> repay(
            @RequestParam String loanId,
            @RequestParam BigDecimal amount) {

        return ResponseEntity.ok(
                loanService.repayLoan(loanId, amount)
        );
    }

    @GetMapping("/history")
    public ResponseEntity<?> history(@RequestParam String accountId) {
        return ResponseEntity.ok(
                loanService.getLoans(accountId)
        );
    }
}
