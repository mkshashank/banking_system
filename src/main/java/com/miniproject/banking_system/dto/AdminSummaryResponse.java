package com.miniproject.banking_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminSummaryResponse {
    private long totalCustomers;
    private double totalDeposits;
    private long topAccountsCount;
    private int totalLoanRequests;
    private int eligibleLoans;
    private int ineligibleLoans;
}
