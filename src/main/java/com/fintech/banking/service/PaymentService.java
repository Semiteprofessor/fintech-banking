package com.fintech.banking.service;

import com.fintech.banking.constants.TransactionStatus;
import com.fintech.banking.constants.TransactionType;
import com.fintech.banking.dto.TransferRequest;
import com.fintech.banking.model.Account;
import com.fintech.banking.model.Payment;
import com.fintech.banking.model.Transaction;
import com.fintech.banking.repository.AccountRepository;
import com.fintech.banking.repository.PaymentRepository;
import com.fintech.banking.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final AccountRepository accountRepository;
    private final PaymentRepository paymentRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public String makePayment(String senderAccountNumber,
                              String beneficiaryAccountNumber,
                              BigDecimal amount) {

        Account sender = accountRepository.findByAccountNumber(senderAccountNumber)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        Account beneficiary = accountRepository.findByAccountNumber(beneficiaryAccountNumber)
                .orElseThrow(() -> new RuntimeException("Beneficiary account not found"));

        if (sender.getAccountNumber().equals(beneficiary.getAccountNumber())) {
            throw new RuntimeException("Cannot transfer to same account");
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        String reference = UUID.randomUUID().toString();

        sender.setBalance(sender.getBalance().subtract(amount));
        beneficiary.setBalance(beneficiary.getBalance().add(amount));

        accountRepository.save(sender);
        accountRepository.save(beneficiary);

        Payment payment = new Payment();
        payment.setAccountId(sender.getAccountId());
        payment.setBeneficiary(beneficiary.getAccountName());
        payment.setBeneficiaryAccountId(beneficiary.getAccountId());
        payment.setAmount(amount);
        payment.setReferenceNumber(reference);
        payment.setStatus("SUCCESS");
        payment.setReasonCode("00");

        paymentRepository.save(payment);

        Transaction debit = new Transaction();
        debit.setAccount(sender);
        debit.setUserId(sender.getUser().getUserId());
        debit.setTransactionType(TransactionType.DEBIT);
        debit.setAmount(amount);
        debit.setChannel("TRANSFER");
        debit.setStatus(TransactionStatus.SUCCESS);
        debit.setReference(reference);
        debit.setBalanceAfter(sender.getBalance());

        transactionRepository.save(debit);

        Transaction credit = new Transaction();
        credit.setAccount(beneficiary);
        credit.setUserId(beneficiary.getUser().getUserId());
        credit.setTransactionType(TransactionType.CREDIT);
        credit.setAmount(amount);
        credit.setChannel("TRANSFER");
        credit.setStatus(TransactionStatus.SUCCESS);
        credit.setReference(reference);
        credit.setBalanceAfter(beneficiary.getBalance());

        transactionRepository.save(credit);

        return "Payment successful. Ref: " + reference;
    }

    public String transfer(TransferRequest request) {

        Account sender = accountRepository.findById(request.getSourceAccountId())
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        Account beneficiary = accountRepository.findByAccountNumber(
                request.getDestinationAccountNumber()
        ).orElseThrow(() -> new RuntimeException("Beneficiary account not found"));

        return makePayment(
                sender.getAccountNumber(),
                beneficiary.getAccountNumber(),
                request.getAmount()
        );
    }

    public boolean validateAccount(String accountNumber) {

        return accountRepository.findByAccountNumber(accountNumber)
                .isPresent();
    }
}