package com.miniproject.banking_system.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Slf4j
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double balance;

    private LocalDateTime createdAt;  // 🕒 Tracks account creation date/time

    public Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
        this.createdAt = LocalDateTime.now();
        log.debug("Account created: name={}, balance={}, createdAt={}", name, balance, createdAt);
    }
}
