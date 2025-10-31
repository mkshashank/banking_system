package com.miniproject.banking_system.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Entity
@Slf4j
@Data
public class Account
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double balance;

    public Account() {
        log.debug("Account: No-arg constructor called");
    }

    public Account(String name, double balance) {
        log.debug("Account: Parameterized constructor called with name = {}, balance = {}", name, balance);
        this.name = name;
        this.balance = balance;
    }
}