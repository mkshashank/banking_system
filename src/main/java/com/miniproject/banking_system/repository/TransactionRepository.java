package com.miniproject.banking_system.repository;

import com.miniproject.banking_system.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // ⭐ Fetch all transactions for an account
    List<Transaction> findByAccount_IdOrderByTimestampDesc(Long accountId);

    // ⭐ Fetch transactions for a time-range
    List<Transaction> findByAccount_IdAndTimestampBetweenOrderByTimestampAsc(
            Long accountId, LocalDateTime startDate, LocalDateTime endDate
    );

    // ⭐ Total deposits across the entire system
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.type = 'DEPOSIT' OR (t.type = 'TRANSFER' AND t.amount > 0)")
    double getTotalDeposits();

    // ⭐ Total withdrawals across the entire system
    @Query("SELECT COALESCE(SUM(ABS(t.amount)), 0) FROM Transaction t " +
            "WHERE t.type = 'WITHDRAW' OR (t.type = 'TRANSFER' AND t.amount < 0)")
    double getTotalWithdrawals();

    // ⭐ Total transaction count in system
    @Query("SELECT COUNT(t) FROM Transaction t")
    long getTotalTransactions();
}
