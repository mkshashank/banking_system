package com.miniproject.banking_system.dto;

import lombok.Data;

@Data
public class CreditCardBillRequest {
    private double totalSpending;   // total billed amount for the cycle
    private double paymentsMade;    // total payments made against that bill
    private String dueDate;         // ISO date string, e.g. "2025-11-30"
    private String currentDate;     // ISO date string, e.g. "2025-12-05"
}
