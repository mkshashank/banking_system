package com.miniproject.banking_system.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class InterestRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double principal;
    private double rate;
    private double timeYears;

    private double interest;
    private double totalAmount;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
