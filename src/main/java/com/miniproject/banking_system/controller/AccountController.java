package com.miniproject.banking_system.controller;

import com.miniproject.banking_system.model.Account;
import com.miniproject.banking_system.model.Transaction;
import com.miniproject.banking_system.service.AccountService;
import com.miniproject.banking_system.dto.TransferRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.miniproject.banking_system.dto.InterestRequest;
import com.miniproject.banking_system.dto.InterestResponse;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
@Slf4j
public class AccountController
{

    @Autowired
    private AccountService accountService;

    @PostMapping
    public Map<String, Object> createAccount(@RequestBody Map<String, Object> request)
    {
        log.info("Received request to create account with data: {}", request);
        String name = (String) request.get("name");
        double initialDeposit = ((Number) request.get("initialDeposit")).doubleValue();
        Account account = accountService.createAccount(name, initialDeposit);
        log.info("Account created successfully. Account ID: {}, Balance: {}", account.getId()
                , account.getBalance());
        return Map.of("accountId", account.getId(), "balance", account.getBalance());
    }

    @GetMapping("/{id}")
    public Account getAccount(@PathVariable Long id)
    {
        log.info("Fetching account details for Account ID: {}", id);
        Account account = accountService.getAccount(id);
        log.info("Account details retrieved: {}", account);
        return account;
    }

    @GetMapping
    public List<Account> getAllAccounts()
    {
        log.info("Fetching all account details");
        List<Account> accounts = accountService.getAllAccounts();
        log.info("Total accounts retrieved: {}", accounts.size());
        return accounts;
    }

    @PostMapping("/{id}/deposit")
    public Map<String, Object> deposit(@PathVariable Long id, @RequestBody Map<String, Object> request)
    {
        log.info("Received deposit request for Account ID: {} with data: {}", id, request);
        double amount = ((Number) request.get("amount")).doubleValue();
        Account account = accountService.deposit(id, amount);
        log.info("Deposit successful. New balance for Account ID {}: {}", account.getId()
                , account.getBalance());
        return Map.of("accountId", account.getId(), "balance", account.getBalance());
    }

    @PostMapping("/{id}/withdraw")
    public Map<String, Object> withdraw(@PathVariable Long id, @RequestBody Map<String, Object> request)
    {
        log.info("Received withdrawal request for Account ID: {} with data: {}", id, request);
        double amount = ((Number) request.get("amount")).doubleValue();
        Account account = accountService.withdraw(id, amount);
        log.info("Withdrawal successful. New balance for Account ID {}: {}", account.getId()
                , account.getBalance());
        return Map.of("accountId", account.getId(), "balance", account.getBalance());
    }

    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferRequest request)
    {
        log.info("Received transfer request: {}", request);
        String result = accountService.transferFunds(
                request.getFromAccount(),
                request.getToAccount(),
                request.getAmount()
        );
        log.info("Transfer result: {}", result);
        return result;
    }

    @GetMapping("/transactions/{accountId}")
    public List<Transaction> getTransactions(@PathVariable Long accountId)
    {
        log.info("Received request to fetch transactions for Account ID: {}", accountId);
        List<Transaction> transactions = accountService.getTransactions(accountId);
        log.info("Total transactions retrieved for Account ID {}: {}", accountId, transactions.size());
        return transactions;
    }

}