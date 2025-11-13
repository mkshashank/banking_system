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
     * Ledger-based design:
     *  - Account always starts with 0 balance.
     *  - Initial deposit is done using deposit() method.
     */
    public Account createAccount(String name, double initialDeposit) {
        log.info("Creating account for: {} with initial deposit: {}", name, initialDeposit);

        // Step 1️⃣: Create the account with zero balance
        Account account = new Account(name, 0.0);
        account.setCreatedAt(LocalDateTime.now());
        account = accountRepository.save(account);

        // Step 2️⃣: If initial deposit is provided, record it as a deposit transaction
        if (initialDeposit > 0) {
            log.info("Recording initial deposit of ₹{} for Account ID: {}", initialDeposit, account.getId());
            deposit(account.getId(), initialDeposit);
        }

        log.info("✅ Account created successfully. ID: {}, Balance: {}", account.getId(), account.getBalance());
        return account;
    }

    /**
     * Get all accounts.
     */
    public List<Account> getAllAccounts() {
        log.info("Fetching all accounts");
        List<Account> accounts = accountRepository.findAll();
        log.info("Total accounts found: {}", accounts.size());
        return accounts;
    }

    /**
     * Get account by ID.
     */
    public Account getAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    /**
     * Deposit money into an account.
     */
    @Transactional
    public Account deposit(Long id, double amount) {
        log.info("Depositing ₹{} into Account ID: {}", amount, id);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Transaction transaction = new Transaction(id, TransactionType.DEPOSIT, amount);
        transactionRepository.save(transaction);

        log.info("✅ Deposit successful. New balance: {}", account.getBalance());
        return account;
    }

    /**
     * Withdraw money from an account.
     */
    @Transactional
    public Account withdraw(Long id, double amount) {
        log.info("Withdrawing ₹{} from Account ID: {}", amount, id);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }

        if (account.getBalance() < amount) {
            throw new InsufficientFundsException(amount);
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        Transaction transaction = new Transaction(id, TransactionType.WITHDRAW, amount);
        transactionRepository.save(transaction);

        log.info("✅ Withdrawal successful. Remaining balance: {}", account.getBalance());
        return account;
    }

    /**
     * Transfer funds between two accounts atomically.
     */
    @Transactional
    public String transferFunds(Long fromId, Long toId, double amount) {
        log.info("Transferring ₹{} from Account ID {} → Account ID {}", amount, fromId, toId);

        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("Source and destination accounts cannot be the same.");
        }

        Account fromAccount = accountRepository.findById(fromId)
                .orElseThrow(() -> new AccountNotFoundException(fromId));

        Account toAccount = accountRepository.findById(toId)
                .orElseThrow(() -> new AccountNotFoundException(toId));

        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive.");
        }

        if (fromAccount.getBalance() < amount) {
            throw new InsufficientFundsException(amount);
        }

        // Debit source account
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        accountRepository.save(fromAccount);
        transactionRepository.save(new Transaction(fromId, TransactionType.TRANSFER, -amount));

        // Credit destination account
        toAccount.setBalance(toAccount.getBalance() + amount);
        accountRepository.save(toAccount);
        transactionRepository.save(new Transaction(toId, TransactionType.TRANSFER, amount));

        log.info("✅ Transfer complete. Source new balance: {}", fromAccount.getBalance());
        return "Transfer Successful. Remaining balance: " + fromAccount.getBalance();
    }

    /**
     * Get all transactions for a specific account.
     */
    public List<Transaction> getTransactions(Long accountId) {
        log.info("Fetching transactions for Account ID: {}", accountId);
        List<Transaction> transactions = transactionRepository.findByAccountIdOrderByTimestampDesc(accountId);
        log.info("Total transactions fetched: {}", transactions.size());
        return transactions;
    }
}
