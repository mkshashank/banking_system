package com.miniproject.banking_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreditCardBillResponse {
    private double pendingAmount;
    private double interest;
    private double lateFee;
    private double totalDue;
    private String status;   // "Paid On Time", "Pending (Not Due)", "Paid (Late)", "Overdue"
    private int daysDelayed; // 0 if not delayed
}
