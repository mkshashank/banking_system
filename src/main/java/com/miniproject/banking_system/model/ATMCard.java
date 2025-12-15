package com.miniproject.banking_system.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "atmcard")
@Data
@NoArgsConstructor
public class ATMCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_number", nullable = false, unique = true)
    private String cardNumber;

    @Column(nullable = false)
    private String pin;

    @Column(name = "daily_limit", nullable = false)
    private double dailyLimit;

    @Column(name = "withdrawn_today", nullable = false)
    private double withdrawnToday = 0;

    @Column(name = "pin_retry_count", nullable = false)
    private int pinRetryCount = 0;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;  // Stored as DATE → JPA converts automatically

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;


    // Custom constructor (useful for testing or bulk creation)
    public ATMCard(
            String cardNumber,
            String pin,
            double dailyLimit,
            LocalDate expiryDate,
            Account account
    ) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.dailyLimit = dailyLimit;
        this.expiryDate = expiryDate;
        this.account = account;
    }
}
