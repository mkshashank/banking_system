package com.miniproject.banking_system.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    private Long accountId;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private double amount;

    private LocalDateTime timestamp;

    public Transaction() {}

    public Transaction(Long accountId, TransactionType type, double amount)
    {
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }
}
