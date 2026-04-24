package com.fintech.banking.controller;

import com.fintech.banking.dto.request.BillPaymentRequest;
import com.fintech.banking.service.BillPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class BillPaymentController {

    private final BillPaymentService billPaymentService;

    @PostMapping("/pay")
    public ResponseEntity<?> payBill(@RequestBody BillPaymentRequest request) {
        return ResponseEntity.ok(
                Map.of("message", billPaymentService.payBill(request))
        );
    }

    @GetMapping("/history")
    public ResponseEntity<?> getHistory(@RequestParam String accountId) {
        return ResponseEntity.ok(
                billPaymentService.getBillHistory(accountId)
        );
    }
}
