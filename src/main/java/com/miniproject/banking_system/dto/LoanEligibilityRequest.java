package com.miniproject.banking_system.dto;

import lombok.Data;

@Data
public class LoanEligibilityRequest {
    private int age;
    private double annualIncome;
    private int creditScore;
    private double existingLoanAmount;
}