package com.miniproject.banking_system.dto;

import lombok.Data;

@Data
public class FixedDepositRequest {
    private double amount;
    private double rate;   // Annual interest rate in %
    private int tenure;    // Years
    private boolean prematureWithdrawal; // optional flag (default false)
}
