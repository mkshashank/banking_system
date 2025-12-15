package com.miniproject.banking_system.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    private double amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private LocalDateTime timestamp = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "account_id")
    @ToString.Exclude
    private Account account;

    // ⭐ REQUIRED BY AccountService
    public Transaction(Account account, TransactionType type, double amount) {
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }
}
