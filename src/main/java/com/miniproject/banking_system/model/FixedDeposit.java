package com.miniproject.banking_system.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class FixedDeposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ⭐ Foreign Key → Account
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private double amount;
    private double rate;
    private int tenure;

    private boolean prematureWithdrawal;

    private double maturityAmount;
    private double interestEarned;

    private String statusMessage;
}
