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
import com.miniproject.banking_system.dto.InterestResponse;
import com.miniproject.banking_system.dto.InterestRequest;

import java.util.List;

@Service
@Slf4j
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    private void logTransaction(Long accountId, TransactionType type, double amount)
    {
        Transaction transaction = new Transaction(accountId, type, amount);
        transactionRepository.save(transaction);
    }


    public Account createAccount(String name, double initialDeposit) {
        log.info("Creating account for name: {}, initialDeposit: {}", name, initialDeposit);
        Account account = new Account(name, initialDeposit);
        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully with ID: {}", savedAccount.getId());
        return savedAccount;
    }

    public List<Account> getAllAccounts() {
        log.info("Fetching all accounts");
        List<Account> accounts = accountRepository.findAll();
        log.info("Total accounts retrieved: {}", accounts.size());
        return accounts;
    }

    public Account getAccount(Long id) {
        log.info("Fetching account with ID: {}", id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Account with ID {} not found", id);
                    return new AccountNotFoundException(id);
                });
        log.info("Account retrieved: {}", account);
        return account;
    }

    public Account deposit(Long id, double amount)
    {
        log.info("Depositing amount: {} to account ID: {}", amount, id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                {
                    log.warn("Account with ID {} not found for deposit", id);
                    return new AccountNotFoundException(id);
                });
        account.setBalance(account.getBalance() + amount);
        Account updatedAccount = accountRepository.save(account);

        logTransaction(id, TransactionType.DEPOSIT, amount);
        log.info("Deposit successful. New balance: {}", updatedAccount.getBalance());
        return updatedAccount;
    }

    public Account withdraw(Long id, double amount)
    {
        log.info("Withdrawing amount: {} from account ID: {}", amount, id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                {
                    log.warn("Account with ID {} not found for withdrawal", id);
                    return new AccountNotFoundException(id);
                });
        if (account.getBalance() < amount)
        {
            log.warn("Insufficient funds for withdrawal. Account ID: {}, Balance: {}, Requested: {}", id, account.getBalance(), amount);
            throw new InsufficientFundsException(amount);
        }
        account.setBalance(account.getBalance() - amount);
        Account updatedAccount = accountRepository.save(account);

        logTransaction(id, TransactionType.WITHDRAW, amount);
        log.info("Withdrawal successful. New balance: {}", updatedAccount.getBalance());
        return updatedAccount;
    }

    @Transactional
    public String transferFunds(Long fromId, Long toId, double amount)
    {
        log.info("Initiating transfer of amount: {} from Account ID: {} to Account ID: {}", amount, fromId, toId);

        Account fromAccount = accountRepository.findById(fromId)
                .orElseThrow(() ->
                {
                    log.warn("Source account with ID {} not found", fromId);
                    return new AccountNotFoundException(fromId);
                });

        Account toAccount = accountRepository.findById(toId)
                .orElseThrow(() ->
                {
                    log.warn("Destination account with ID {} not found", toId);
                    return new AccountNotFoundException(toId);
                });

        if (fromAccount.getBalance() < amount)
        {
            log.warn("Insufficient funds in source account. Balance: {}, Requested: {}", fromAccount.getBalance(), amount);
            throw new InsufficientFundsException(amount);
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        logTransaction(fromId, TransactionType.TRANSFER, -amount);
        logTransaction(toId,TransactionType.TRANSFER,amount);
        log.info("Transfer successful. New balance of source account (ID: {}): {}", fromId, fromAccount.getBalance());
        return "Transfer Successful. Remaining balance: " + fromAccount.getBalance();
    }


    public List<Transaction> getTransactions(Long accountId)
    {
        log.info("Fetching transactions for Account ID: {}", accountId);
        List<Transaction> transactions = transactionRepository.findByAccountIdOrderByTimestampDesc(accountId);
        log.info("Total transactions retrieved for Account ID {}: {}", accountId, transactions.size());
        return transactions;
    }



}
