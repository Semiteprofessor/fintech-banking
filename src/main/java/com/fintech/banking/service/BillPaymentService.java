package com.fintech.banking.service;

import com.fintech.banking.constants.TransactionStatus;
import com.fintech.banking.constants.TransactionType;
import com.fintech.banking.dto.request.BillPaymentRequest;
import com.fintech.banking.model.Account;
import com.fintech.banking.model.BillPayment;
import com.fintech.banking.model.Transaction;
import com.fintech.banking.repository.AccountRepository;
import com.fintech.banking.repository.BillPaymentRepository;
import com.fintech.banking.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillPaymentService {

    private final AccountRepository accountRepository;
    private final BillPaymentRepository billPaymentRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public String payBill(BillPaymentRequest request) {

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Deduct balance
        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        String reference = UUID.randomUUID().toString();

        // Save bill payment
        BillPayment bill = new BillPayment();
        bill.setAccount(account);
        bill.setAmount(request.getAmount());
        bill.setBillType(request.getBillType());
        bill.setProvider(request.getProvider());
        bill.setCustomerReference(request.getCustomerRef());
        bill.setStatus("SUCCESS");
        bill.setReference(reference);

        billPaymentRepository.save(bill);

        // Save transaction
        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setUserId(account.getUser().getUserId());
        tx.setTransactionType(TransactionType.DEBIT);
        tx.setAmount(request.getAmount());
        tx.setChannel(request.getBillType().name());
        tx.setStatus(TransactionStatus.SUCCESS);
        tx.setReference(reference);
        tx.setBalanceAfter(account.getBalance());

        transactionRepository.save(tx);

        return request.getBillType() + " payment successful";
    }

    public List<BillPayment> getBillHistory(String accountId) {
        return billPaymentRepository
                .findByAccount_AccountIdOrderByCreatedAtDesc(accountId);
    }
}
