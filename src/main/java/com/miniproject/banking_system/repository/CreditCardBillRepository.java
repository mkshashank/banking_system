package com.miniproject.banking_system.repository;

import com.miniproject.banking_system.model.CreditCardBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardBillRepository extends JpaRepository<CreditCardBill, Long> {

    // Fetch the latest bill for a given account
    CreditCardBill findTopByAccountIdOrderByIdDesc(Long accountId);
}
