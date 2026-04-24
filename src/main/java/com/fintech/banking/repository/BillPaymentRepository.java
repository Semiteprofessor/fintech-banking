package com.fintech.banking.repository;

import com.fintech.banking.model.BillPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillPaymentRepository extends JpaRepository<BillPayment, String> {

    List<BillPayment> findByAccount_AccountIdOrderByCreatedAtDesc(String accountId);
}
