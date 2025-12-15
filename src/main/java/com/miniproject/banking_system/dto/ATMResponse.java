package com.miniproject.banking_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ATMResponse {

    private String message;   // e.g., CARD_VALID, PIN_VALID, WITHDRAW_SUCCESS
    private Long accountId;   // linked account
    private Double balance;   // optional (nullable), returned for withdraw and card validation
}
