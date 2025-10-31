package com.miniproject.banking_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanEligibilityResponse {
    private String status;
    private String reason;
    private double maxLoanAmount;
}