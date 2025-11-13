package com.miniproject.banking_system.repository;

import com.miniproject.banking_system.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountIdOrderByTimestampDesc(Long accountId);

    List<Transaction> findByAccountIdAndTimestampBetweenOrderByTimestampAsc(
            Long accountId, LocalDateTime startDate, LocalDateTime endDate);

    // ✅ Total deposits across system (DEPOSIT or TRANSFER-IN)
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.type = 'DEPOSIT' OR (t.type = 'TRANSFER' AND t.amount > 0)")
    double getTotalDeposits();

    // ✅ Count total transactions in system
    @Query("SELECT COUNT(t) FROM Transaction t")
    long getTotalTransactions();

    // ✅ Total withdrawals (including negative transfers)
    @Query("SELECT COALESCE(SUM(ABS(t.amount)), 0) FROM Transaction t " +
            "WHERE t.type = 'WITHDRAW' OR (t.type = 'TRANSFER' AND t.amount < 0)")
    double getTotalWithdrawals();
}
