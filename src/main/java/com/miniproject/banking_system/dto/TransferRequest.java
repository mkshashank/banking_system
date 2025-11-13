package com.miniproject.banking_system.dto;

import lombok.Data;

@Data
public class TransferRequest
{
    private Long fromAccount;
    private Long toAccount;
    private double amount;
}
