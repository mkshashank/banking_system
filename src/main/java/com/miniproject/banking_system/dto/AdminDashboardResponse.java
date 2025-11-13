package com.miniproject.banking_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponse {

    private long totalCustomers;
    private double totalDeposits;
    private double totalWithdrawals;
    private long totalTransactions;
    private long topAccountsCount;

    private int totalLoanRequests;
    private int eligibleLoans;
    private int ineligibleLoans;

    private String systemHealth; // e.g., "ACTIVE", "MAINTENANCE"
}
