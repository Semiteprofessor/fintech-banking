package com.fintech.banking.repository;

import com.fintech.banking.model.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, String> {

    List<PaymentHistory> findByUserId(String userId);

}
