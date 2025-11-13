package com.miniproject.banking_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatementResponse {
    private String month;
    private double openingBalance;
    private double totalDeposits;
    private double totalWithdrawals;
    private double closingBalance;
}
