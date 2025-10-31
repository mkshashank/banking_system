package com.miniproject.banking_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InterestResponse {
    private double interest;
    private double totalAmount;
}