package com.miniproject.banking_system.controller;

import com.miniproject.banking_system.model.Account;
import com.miniproject.banking_system.model.Transaction;
import com.miniproject.banking_system.service.AccountService;
import com.miniproject.banking_system.dto.TransferRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor   // ⭐ Constructor injection (no warnings)
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public Map<String, Object> createAccount(@RequestBody Map<String, Object> request) {
        log.info("Received request to create account: {}", request);

        String name = (String) request.get("name");
        double initialDeposit = ((Number) request.get("initialDeposit")).doubleValue();

        Account account = accountService.createAccount(name, initialDeposit);

        log.info("Account created. ID: {}, Balance: {}", account.getId(), account.getBalance());
        return Map.of(
                "accountId", account.getId(),
                "balance", account.getBalance()
        );
    }

    @GetMapping("/{id}")
    public Account getAccount(@PathVariable Long id) {
        log.info("Fetching account {}", id);

        Account acc = accountService.getAccount(id);

        log.info("Account found: {}", acc);
        return acc;
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        log.info("Fetching all accounts");

        List<Account> list = accountService.getAllAccounts();

        log.info("Total accounts: {}", list.size());
        return list;
    }

    @PostMapping("/{id}/deposit")
    public Map<String, Object> deposit(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        log.info("Deposit request for {} → {}", id, request);

        double amount = ((Number) request.get("amount")).doubleValue();
        Account acc = accountService.deposit(id, amount);

        return Map.of(
                "accountId", acc.getId(),
                "balance", acc.getBalance()
        );
    }

    @PostMapping("/{id}/withdraw")
    public Map<String, Object> withdraw(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        log.info("Withdraw request for {} → {}", id, request);

        double amount = ((Number) request.get("amount")).doubleValue();
        Account acc = accountService.withdraw(id, amount);

        return Map.of(
                "accountId", acc.getId(),
                "balance", acc.getBalance()
        );
    }

    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferRequest req) {
        log.info("Transfer request: {}", req);
        return accountService.transferFunds(
                req.getFromAccount(),
                req.getToAccount(),
                req.getAmount()
        );
    }

    @GetMapping("/transactions/{accountId}")
    public List<Transaction> getTransactions(@PathVariable Long accountId) {
        log.info("Fetching transactions for {}", accountId);

        List<Transaction> list = accountService.getTransactions(accountId);

        log.info("Transactions count for {}: {}", accountId, list.size());
        return list;
    }
}
