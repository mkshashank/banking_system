package com.miniproject.banking_system.dto;

import lombok.Data;

@Data
public class CreditCardBillRequest {

    private Long accountId;          // ⭐ NEW — Required for FK linking

    private double totalSpending;    // Total billed amount
    private double paymentsMade;     // Payments made
    private String dueDate;          // YYYY-MM-DD
    private String currentDate;      // YYYY-MM-DD
}
