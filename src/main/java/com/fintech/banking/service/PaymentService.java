package com.fintech.banking.service;

import com.fintech.banking.constants.TransactionStatus;
import com.fintech.banking.constants.TransactionType;
import com.fintech.banking.dto.request.DepositRequest;
import com.fintech.banking.dto.request.TransferRequest;
import com.fintech.banking.dto.response.BalanceResponse;
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
import java.util.Map;
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

        Account sender = accountRepository.findByAccountNumber(request.getSourceAccountNumber())
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

    public Map<String, Object> validateAccount(String accountNumber) {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return Map.of(
                "valid", true,
                "accountName", account.getAccountName(),
                "accountNumber", account.getAccountNumber()
        );
    }

    @Transactional
    public String deposit(DepositRequest request) {

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance().add(request.getAmount()));

        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setUserId(account.getUser().getUserId());
        transaction.setTransactionType(TransactionType.CREDIT);
        transaction.setAmount(request.getAmount());
        transaction.setChannel("DEPOSIT");
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setReference(UUID.randomUUID().toString());
        transaction.setBalanceAfter(account.getBalance());

        transactionRepository.save(transaction);

        return "Deposit successful";
    }

    public String withdraw(String accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setTransactionType(TransactionType.DEBIT);
        tx.setAmount(amount);
        tx.setChannel("WITHDRAWAL");
        tx.setStatus(TransactionStatus.SUCCESS);
        tx.setReference(UUID.randomUUID().toString());
        tx.setBalanceAfter(account.getBalance());

        transactionRepository.save(tx);

        return "Withdrawal successful";
    }

    public BigDecimal getAccountBalance(String accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return account.getBalance();
    }

    public BalanceResponse checkBalance(String accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        BalanceResponse response = new BalanceResponse();
        response.setAccountId(account.getAccountId());
        response.setAccountNumber(account.getAccountNumber());
        response.setBalance(account.getBalance());

        return response;
    }
}