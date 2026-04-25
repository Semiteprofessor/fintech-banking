package com.fintech.banking.repository;

import com.fintech.banking.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, String> {

    List<Loan> findByAccount_AccountId(String accountId);
}