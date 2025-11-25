package com.miniproject.banking_system.dto;

import lombok.Data;

@Data
public class WithdrawRequest {
    private String cardNumber;
    private double amount;
}
