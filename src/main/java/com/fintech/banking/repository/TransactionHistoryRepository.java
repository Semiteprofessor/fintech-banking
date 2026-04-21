package com.fintech.banking.repository;

import com.fintech.banking.model.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, String> {

    List<TransactionHistory> findByUserIdOrderByCreatedAtDesc(String userId);

}
