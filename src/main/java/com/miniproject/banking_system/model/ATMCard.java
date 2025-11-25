package com.miniproject.banking_system.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ATMCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accountId;      // Link to Account table
    private String cardNumber;   // 16-digit card number
    private String pin;          // 4-digit PIN (encrypted later)
    private boolean active = true;
    private int pinRetryCount = 0;

    private double dailyLimit = 10000;  // ₹10,000 daily limit
    private double withdrawnToday = 0;

    private String expiryDate;  // "2028-12"
}
