package com.miniproject.banking_system.service;

import com.miniproject.banking_system.exception.AccountNotFoundException;
import com.miniproject.banking_system.exception.InsufficientFundsException;
import com.miniproject.banking_system.model.Account;
import com.miniproject.banking_system.model.Transaction;
import com.miniproject.banking_system.model.TransactionType;
import com.miniproject.banking_system.repository.AccountRepository;
import com.miniproject.banking_system.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Create a new bank account.
     * For consistency, account starts with 0 balance,
     * and initial deposit is added as a proper deposit transaction.
     */
    public Account createAccount(String name, double initialDeposit) {

        Account account = new Account(name, 0.0);
        account.setCreatedAt(LocalDateTime.now());
        account = accountRepository.save(account);

        if (initialDeposit > 0) {
            deposit(account.getId(), initialDeposit);
        }

        return account;
    }

    /**
     * Fetch all accounts.
     */
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    /**
     * Get account by ID.
     */
    public Account getAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    /**
     * Deposit money.
     */
    @Transactional
    public Account deposit(Long id, double amount) {

        Account account = getAccount(id);

        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Transaction tx = new Transaction(account, TransactionType.DEPOSIT, amount);
        transactionRepository.save(tx);

        return account;
    }

    /**
     * Withdraw money.
     */
    @Transactional
    public Account withdraw(Long id, double amount) {

        Account account = getAccount(id);

        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }

        if (account.getBalance() < amount) {
            throw new InsufficientFundsException(amount);
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        Transaction tx = new Transaction(account, TransactionType.WITHDRAW, amount);
        transactionRepository.save(tx);

        return account;
    }

    /**
     * Transfer money between accounts.
     */
    @Transactional
    public String transferFunds(Long fromId, Long toId, double amount) {

        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("Source and destination accounts cannot be the same.");
        }

        Account fromAccount = getAccount(fromId);
        Account toAccount = getAccount(toId);

        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive.");
        }

        if (fromAccount.getBalance() < amount) {
            throw new InsufficientFundsException(amount);
        }

        // Debit sender
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        accountRepository.save(fromAccount);
        transactionRepository.save(new Transaction(fromAccount, TransactionType.TRANSFER, -amount));

        // Credit receiver
        toAccount.setBalance(toAccount.getBalance() + amount);
        accountRepository.save(toAccount);
        transactionRepository.save(new Transaction(toAccount, TransactionType.TRANSFER, amount));

        return "Transfer Successful. Remaining balance: " + fromAccount.getBalance();
    }

    /**
     * Fetch all transactions for an account.
     */
    public List<Transaction> getTransactions(Long accountId) {
        return transactionRepository.findByAccount_IdOrderByTimestampDesc(accountId);
    }
}
