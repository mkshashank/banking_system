package com.miniproject.banking_system.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(Long id) {
        super("Account with ID " + id + " not found");
        log.warn("AccountNotFoundException thrown for ID: {}", id);
    }
}