package com.fintech.banking.repository;

;
import com.fintech.banking.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    List<Payment> findByAccountId(String accountId);

}
