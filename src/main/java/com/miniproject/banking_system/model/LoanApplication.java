package com.miniproject.banking_system.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK → Account
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private int age;
    private double annualIncome;
    private int creditScore;
    private double existingLoanAmount;

    private String status;            // Eligible or Not Eligible
    private String reason;            // Failure reason if any
    private double maxLoanAmount;

    private LocalDateTime createdAt;

    public LoanApplication(Account account,
                           int age,
                           double annualIncome,
                           int creditScore,
                           double existingLoanAmount,
                           String status,
                           String reason,
                           double maxLoanAmount) {

        this.account = account;
        this.age = age;
        this.annualIncome = annualIncome;
        this.creditScore = creditScore;
        this.existingLoanAmount = existingLoanAmount;

        this.status = status;
        this.reason = reason;
        this.maxLoanAmount = maxLoanAmount;

        this.createdAt = LocalDateTime.now();
    }
}
