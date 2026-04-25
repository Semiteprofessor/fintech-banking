package com.fintech.banking.service;

import com.fintech.banking.dto.request.LoanRequest;
import com.fintech.banking.dto.response.LoanResponse;
import com.fintech.banking.model.Account;
import com.fintech.banking.model.Loan;
import com.fintech.banking.repository.AccountRepository;
import com.fintech.banking.repository.LoanRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final AccountRepository accountRepository;

    public LoanResponse applyLoan(LoanRequest request) {

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        BigDecimal interestRate = new BigDecimal("0.10"); // 10%
        BigDecimal totalRepayment = request.getAmount()
                .add(request.getAmount().multiply(interestRate));

        Loan loan = new Loan();
        loan.setAccount(account);
        loan.setAmount(request.getAmount());
        loan.setInterestRate(interestRate);
        loan.setTotalRepayment(totalRepayment);
        loan.setStatus("PENDING");
        loan.setCreatedAt(LocalDateTime.now());
        loan.setDueDate(LocalDateTime.now().plusMonths(1));

        loanRepository.save(loan);

        return mapToResponse(loan);
    }

    @Transactional
    public String approveLoan(String loanId) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (!loan.getStatus().equals("PENDING")) {
            throw new RuntimeException("Loan already processed");
        }

        Account account = loan.getAccount();

        account.setBalance(account.getBalance().add(loan.getAmount()));

        loan.setStatus("APPROVED");

        return "Loan approved and disbursed";
    }

    @Transactional
    public String repayLoan(String loanId, BigDecimal amount) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        Account account = loan.getAccount();

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));

        BigDecimal remaining = loan.getTotalRepayment().subtract(amount);

        if (remaining.compareTo(BigDecimal.ZERO) <= 0) {
            loan.setStatus("PAID");
        } else {
            loan.setTotalRepayment(remaining);
        }

        return "Loan repayment successful";
    }

    public List<LoanResponse> getLoans(String accountId) {

        return loanRepository.findByAccount_AccountId(accountId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private LoanResponse mapToResponse(Loan loan) {
        return LoanResponse.builder()
                .loanId(loan.getLoanId())
                .amount(loan.getAmount())
                .interestRate(loan.getInterestRate())
                .totalRepayment(loan.getTotalRepayment())
                .status(loan.getStatus())
                .dueDate(loan.getDueDate())
                .createdAt(loan.getCreatedAt())
                .build();
    }
}