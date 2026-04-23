package com.fintech.banking.service;

import com.fintech.banking.constants.AccountType;
import com.fintech.banking.dto.response.AccountResponse;
import com.fintech.banking.helper.GenerateAccountNumber;
import com.fintech.banking.model.Account;
import com.fintech.banking.model.User;
import com.fintech.banking.repository.AccountRepository;
import com.fintech.banking.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public Account createAccount(String userId, String accountName, String accountType) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = new Account();
        account.setUser(user);
        account.setAccountName(accountName);
        account.setAccountType(AccountType.valueOf(accountType));

        account.setAccountNumber(
                GenerateAccountNumber.generateAccountNumber()
        );

        account.setBalance(BigDecimal.ZERO);

        return accountRepository.save(account);
    }

    public List<Account> getUserAccounts(String userId) {
        return accountRepository.findByUserUserId(userId);
    }

    public BigDecimal getAccountBalance(String accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return account.getBalance();
    }

    @Transactional
    public String transfer(String fromAccountNumber,
                           String toAccountNumber,
                           BigDecimal amount) {

        Account sender = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        Account receiver = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        if (sender.getAccountNumber().equals(receiver.getAccountNumber())) {
            throw new RuntimeException("Cannot transfer to same account");
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance().subtract(amount));

        receiver.setBalance(receiver.getBalance().add(amount));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        return "Transfer successful";
    }

    public AccountResponse getAccountById(String accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return mapToResponse(account);
    }

    private AccountResponse mapToResponse(Account account) {

        AccountResponse response = new AccountResponse();

        response.setAccountId(account.getAccountId());
        response.setAccountNumber(account.getAccountNumber());
        response.setAccountName(account.getAccountName());
        response.setAccountType(String.valueOf(AccountType.SAVINGS));
        response.setBalance(account.getBalance());
        response.setCreatedAt(account.getCreatedAt());

        return response;
    }
}