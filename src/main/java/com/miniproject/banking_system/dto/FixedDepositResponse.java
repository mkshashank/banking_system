package com.miniproject.banking_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FixedDepositResponse {
    private double maturityAmount;
    private double interestEarned;
    private String message;
}
