package com.miniproject.banking_system.repository;

import com.miniproject.banking_system.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>
{
    List<Transaction> findByAccountIdOrderByTimestampDesc(Long accountId);
}
