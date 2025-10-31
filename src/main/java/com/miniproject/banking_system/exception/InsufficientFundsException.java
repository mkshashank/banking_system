package com.miniproject.banking_system.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(double amount) {
        super("Insufficient balance for withdrawal of amount: " + amount);
        log.warn("InsufficientFundsException thrown for withdrawal amount: {}", amount);
    }
}