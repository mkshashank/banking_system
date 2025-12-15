package com.miniproject.banking_system.dto;

import lombok.Data;

@Data
public class FixedDepositRequest {

    private Long accountId;           // ⭐ NEW — required for FK relationship

    private double amount;            // principal
    private double rate;              // annual interest rate (%)
    private int tenure;               // years
    private boolean prematureWithdrawal; // optional (default false)
}
