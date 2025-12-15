package com.miniproject.banking_system.dto;

import lombok.Data;

@Data
public class LoanEligibilityRequest {

    private Long accountId;           // ⭐ NEW — required to link loan application to account

    private int age;
    private double annualIncome;
    private int creditScore;
    private double existingLoanAmount;
}
