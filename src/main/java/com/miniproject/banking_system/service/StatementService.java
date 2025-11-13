package com.miniproject.banking_system.service;

import com.miniproject.banking_system.dto.StatementResponse;
import com.miniproject.banking_system.exception.AccountNotFoundException;
import com.miniproject.banking_system.model.Account;
import com.miniproject.banking_system.model.Transaction;
import com.miniproject.banking_system.model.TransactionType;
import com.miniproject.banking_system.repository.AccountRepository;
import com.miniproject.banking_system.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;

@Service
@Slf4j
public class StatementService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    public StatementResponse generateMonthlyStatement(Long accountId, int month, int year) {
        log.info("Generating monthly statement for Account ID: {}, Month: {}, Year: {}", accountId, month, year);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        // Define time range for the month
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        // Fetch transactions
        List<Transaction> allTransactions = transactionRepository.findByAccountIdOrderByTimestampDesc(accountId);
        List<Transaction> monthTransactions = transactionRepository
                .findByAccountIdAndTimestampBetweenOrderByTimestampAsc(accountId, startDate, endDate);

        // ---- Opening Balance (credits − debits before startDate)
        double totalCreditsBefore = allTransactions.stream()
                .filter(t -> t.getTimestamp().isBefore(startDate))
                .filter(t -> t.getType() == TransactionType.DEPOSIT
                        || (t.getType() == TransactionType.TRANSFER && t.getAmount() > 0))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalDebitsBefore = allTransactions.stream()
                .filter(t -> t.getTimestamp().isBefore(startDate))
                .filter(t -> t.getType() == TransactionType.WITHDRAW
                        || (t.getType() == TransactionType.TRANSFER && t.getAmount() < 0))
                .mapToDouble(t -> Math.abs(t.getAmount()))
                .sum();

        double openingBalance = totalCreditsBefore - totalDebitsBefore;

        // ---- Monthly totals
        double totalDeposits = monthTransactions.stream()
                .filter(t -> t.getType() == TransactionType.DEPOSIT
                        || (t.getType() == TransactionType.TRANSFER && t.getAmount() > 0))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalWithdrawals = monthTransactions.stream()
                .filter(t -> t.getType() == TransactionType.WITHDRAW
                        || (t.getType() == TransactionType.TRANSFER && t.getAmount() < 0))
                .mapToDouble(t -> Math.abs(t.getAmount()))
                .sum();

        double closingBalance = openingBalance + totalDeposits - totalWithdrawals;

        StatementResponse response = new StatementResponse(
                Month.of(month).name(),
                round(openingBalance),
                round(totalDeposits),
                round(totalWithdrawals),
                round(closingBalance)
        );

        log.info("Monthly statement generated successfully for Account ID {}: {}", accountId, response);
        return response;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
