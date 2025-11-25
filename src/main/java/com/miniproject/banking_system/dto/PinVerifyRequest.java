package com.miniproject.banking_system.dto;

import lombok.Data;

@Data
public class PinVerifyRequest {
    private String cardNumber;
    private String pin;
}
