package com.miniproject.banking_system.service;

import com.miniproject.banking_system.dto.AdminDashboardResponse;
import com.miniproject.banking_system.dto.AdminSummaryResponse;
import com.miniproject.banking_system.model.Account;
import com.miniproject.banking_system.repository.AccountRepository;
import com.miniproject.banking_system.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AdminService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Fetch total number of customers directly from DB using COUNT()
     */
    public long getTotalCustomers() {
        long count = accountRepository.getTotalCustomers();
        log.info("Total customers in system: {}", count);
        return count;
    }

    /**
     * Fetch total deposits directly from DB using SUM()
     */
    public double getTotalDeposits() {
        double total = transactionRepository.getTotalDeposits();
        log.info("Total deposits (system-wide): ₹{}", total);
        return total;
    }

    /**
     * Get list of top accounts (balance > 1 lakh)
     */
    public List<Account> getTopAccounts() {
        List<Account> topAccounts = accountRepository.getTopAccounts();
        log.info("Top accounts found: {}", topAccounts.size());
        return topAccounts;
    }

    /**
     * Build a combined summary response (aggregated view)
     */
    public AdminSummaryResponse getLoanSummary() {
        long totalCustomers = accountRepository.getTotalCustomers();
        double totalDeposits = transactionRepository.getTotalDeposits();
        long topAccountsCount = accountRepository.countTopAccounts();

        // Simulated loan data (replace with real DB logic when available)
        int totalLoanRequests = 8;
        int eligibleLoans = 5;
        int ineligibleLoans = totalLoanRequests - eligibleLoans;

        log.info("Loan summary: totalRequests={}, eligible={}, ineligible={}",
                totalLoanRequests, eligibleLoans, ineligibleLoans);

        return new AdminSummaryResponse(
                totalCustomers,
                totalDeposits,
                topAccountsCount,
                totalLoanRequests,
                eligibleLoans,
                ineligibleLoans
        );
    }

    /**
     * Combined dashboard summary — aggregates all core metrics
     */
    public AdminDashboardResponse getDashboardSummary() {
        long totalCustomers = accountRepository.getTotalCustomers();
        double totalDeposits = transactionRepository.getTotalDeposits();
        double totalWithdrawals = transactionRepository.getTotalWithdrawals();
        long totalTransactions = transactionRepository.getTotalTransactions();
        long topAccountsCount = accountRepository.countTopAccounts();

        // Simulated loan data — replace later with real DB integration
        int totalLoanRequests = 8;
        int eligibleLoans = 5;
        int ineligibleLoans = totalLoanRequests - eligibleLoans;

        String systemHealth = "ACTIVE"; // could later check DB connection, queue status, etc.

        log.info("Dashboard Summary → customers={}, deposits={}, withdrawals={}, transactions={}, topAccounts={}, loans={}/{}",
                totalCustomers, totalDeposits, totalWithdrawals, totalTransactions, topAccountsCount, eligibleLoans, totalLoanRequests);

        return new AdminDashboardResponse(
                totalCustomers,
                totalDeposits,
                totalWithdrawals,
                totalTransactions,
                topAccountsCount,
                totalLoanRequests,
                eligibleLoans,
                ineligibleLoans,
                systemHealth
        );
    }
}
