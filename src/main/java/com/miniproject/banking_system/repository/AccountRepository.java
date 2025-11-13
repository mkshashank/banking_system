package com.miniproject.banking_system.repository;

import com.miniproject.banking_system.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    // ✅ Count total customers
    @Query("SELECT COUNT(a) FROM Account a")
    long getTotalCustomers();

    // ✅ Fetch accounts with balance > ₹1L
    @Query("SELECT a FROM Account a WHERE a.balance > 10000")
    List<Account> getTopAccounts();

    // ✅ Count how many accounts have balance > ₹1L
    @Query("SELECT COUNT(a) FROM Account a WHERE a.balance > 10000")
    long countTopAccounts();
}
