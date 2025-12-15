package com.miniproject.banking_system.service;

import com.miniproject.banking_system.dto.AdminDashboardResponse;
import com.miniproject.banking_system.dto.AdminSummaryResponse;
import com.miniproject.banking_system.model.Account;
import com.miniproject.banking_system.repository.AccountRepository;
import com.miniproject.banking_system.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor   // ⭐ Constructor Injection (no more warnings)
@Slf4j
public class AdminService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Fetch total number of customers using COUNT(*)
     */
    public long getTotalCustomers() {
        long count = accountRepository.getTotalCustomers();
        log.info("Total customers: {}", count);
        return count;
    }

    /**
     * Fetch total deposits from transaction table
     */
    public double getTotalDeposits() {
        double totalDeposits = transactionRepository.getTotalDeposits();
        log.info("Total system deposits: {}", totalDeposits);
        return totalDeposits;
    }

    /**
     * Get all accounts with high balances (> ₹1L)
     */
    public List<Account> getTopAccounts() {
        List<Account> result = accountRepository.getTopAccounts();
        log.info("Top accounts count: {}", result.size());
        return result;
    }

    /**
     * Summary of loans (simulated for now)
     */
    public AdminSummaryResponse getLoanSummary() {

        long totalCustomers = accountRepository.getTotalCustomers();
        double totalDeposits = transactionRepository.getTotalDeposits();
        long topAccountsCount = accountRepository.countTopAccounts();

        // Simulated loan metrics until LoanApplication entity is added
        int totalLoanRequests = 8;
        int eligibleLoans = 5;
        int ineligibleLoans = totalLoanRequests - eligibleLoans;

        log.info("Loan Summary → totalRequests={}, eligible={}, ineligible={}",
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
     * Complete admin dashboard aggregation
     */
    public AdminDashboardResponse getDashboardSummary() {

        long totalCustomers = accountRepository.getTotalCustomers();
        double totalDeposits = transactionRepository.getTotalDeposits();
        double totalWithdrawals = transactionRepository.getTotalWithdrawals();
        long totalTransactions = transactionRepository.getTotalTransactions();
        long topAccountsCount = accountRepository.countTopAccounts();

        int totalLoanRequests = 8;
        int eligibleLoans = 5;
        int ineligibleLoans = totalLoanRequests - eligibleLoans;

        String systemHealth = "ACTIVE";

        log.info("Admin Dashboard → customers={}, deposits={}, withdrawals={}, txns={}, topAccounts={}",
                totalCustomers, totalDeposits, totalWithdrawals, totalTransactions, topAccountsCount);

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
