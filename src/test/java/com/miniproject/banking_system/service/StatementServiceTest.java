package com.miniproject.banking_system.service;

import com.miniproject.banking_system.dto.StatementResponse;
import com.miniproject.banking_system.exception.AccountNotFoundException;
import com.miniproject.banking_system.model.Account;
import com.miniproject.banking_system.model.Transaction;
import com.miniproject.banking_system.model.TransactionType;
import com.miniproject.banking_system.repository.AccountRepository;
import com.miniproject.banking_system.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class StatementServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private StatementService statementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGenerateCorrectMonthlyStatement() {
        Long accountId = 5L;
        int month = 11;
        int year = 2025;

        Account account = new Account("Test User", 16000);
        account.setId(accountId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        LocalDateTime t1 = LocalDateTime.of(2025, 10, 20, 10, 0);
        LocalDateTime t2 = LocalDateTime.of(2025, 11, 5, 10, 0);
        LocalDateTime t3 = LocalDateTime.of(2025, 11, 15, 12, 0);

        Transaction tx1 = new Transaction(account, TransactionType.DEPOSIT, 5000);
        Transaction tx2 = new Transaction(account, TransactionType.DEPOSIT, 3000);
        Transaction tx3 = new Transaction(account, TransactionType.WITHDRAW, 2000);

        tx1.setTimestamp(t1);
        tx2.setTimestamp(t2);
        tx3.setTimestamp(t3);

        // IMPORTANT – USE CORRECT METHOD NAME
        when(transactionRepository.findByAccount_IdOrderByTimestampDesc(accountId))
                .thenReturn(List.of(tx1, tx2, tx3));

        when(transactionRepository.findByAccount_IdAndTimestampBetweenOrderByTimestampAsc(
                eq(accountId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(tx2, tx3));

        StatementResponse response = statementService.generateMonthlyStatement(accountId, month, year);

        assertEquals("NOVEMBER", response.getMonth());
        assertEquals(5000.0, response.getOpeningBalance());
        assertEquals(3000.0, response.getTotalDeposits());
        assertEquals(2000.0, response.getTotalWithdrawals());
        assertEquals(6000.0, response.getClosingBalance());
    }

    @Test
    void shouldHandleNoTransactionsGracefully() {
        Long accountId = 10L;

        Account account = new Account("No Txn User", 5000);
        account.setId(accountId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        when(transactionRepository.findByAccount_IdOrderByTimestampDesc(accountId))
                .thenReturn(List.of());

        when(transactionRepository.findByAccount_IdAndTimestampBetweenOrderByTimestampAsc(
                eq(accountId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());

        StatementResponse response = statementService.generateMonthlyStatement(accountId, 11, 2025);

        assertEquals("NOVEMBER", response.getMonth());
        assertEquals(0.0, response.getOpeningBalance());
        assertEquals(0.0, response.getTotalDeposits());
        assertEquals(0.0, response.getTotalWithdrawals());
        assertEquals(0.0, response.getClosingBalance());
    }

    @Test
    void shouldThrowExceptionIfAccountNotFound() {
        Long accountId = 99L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> statementService.generateMonthlyStatement(accountId, 11, 2025));
    }
}
