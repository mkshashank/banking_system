package com.miniproject.banking_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FixedDepositResponse {

    private Long fdId;               // ⭐ Newly added for persistence
    private double maturityAmount;
    private double interestEarned;
    private String statusMessage;
}
