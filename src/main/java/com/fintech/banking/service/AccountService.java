package com.fintech.banking.service;

import com.fintech.banking.model.Account;
import com.fintech.banking.model.User;
import com.fintech.banking.repository.AccountRepository;
import com.fintech.banking.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    // =========================
    // CREATE ACCOUNT
    // =========================
    public Account createAccount(String userId, String accountName, String accountType) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = new Account();
        account.setUser(user);
        account.setAccountName(accountName);
        account.setAccountType(accountType);

        // 🔢 Generate account number
        account.setAccountNumber(generateAccountNumber());

        // 💰 default balance
        account.setBalance(BigDecimal.ZERO);

        return accountRepository.save(account);
    }

    // =========================
    // GET USER ACCOUNTS
    // =========================
    public List<Account> getUserAccounts(String userId) {
        return accountRepository.findByUser_UserId(userId);
    }

    // =========================
    // GET BALANCE
    // =========================
    public BigDecimal getAccountBalance(String accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return account.getBalance();
    }

    // =========================
    // TRANSFER MONEY
    // =========================
    @Transactional
    public String transfer(String fromAccountNumber,
                           String toAccountNumber,
                           BigDecimal amount) {

        Account sender = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        Account receiver = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        // ❌ prevent self transfer
        if (sender.getAccountNumber().equals(receiver.getAccountNumber())) {
            throw new RuntimeException("Cannot transfer to same account");
        }

        // ❌ insufficient funds
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // 💸 debit sender
        sender.setBalance(sender.getBalance().subtract(amount));

        // 💸 credit receiver
        receiver.setBalance(receiver.getBalance().add(amount));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        return "Transfer successful";
    }

    // =========================
    // ACCOUNT NUMBER GENERATOR
    // =========================
    private String generateAccountNumber() {

        // Example: 044 123 4567890
        String bankCode = "044";
        int middle = (int) (Math.random() * 900 + 100); // 3 digits
        long last = (long) (Math.random() * 9000000000L + 1000000000L); // 10 digits

        return bankCode + " " + middle + " " + last;
    }
}