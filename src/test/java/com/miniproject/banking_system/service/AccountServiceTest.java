package com.miniproject.banking_system.service;

import com.miniproject.banking_system.exception.AccountNotFoundException;
import com.miniproject.banking_system.exception.InsufficientFundsException;
import com.miniproject.banking_system.model.Account;
import com.miniproject.banking_system.model.Transaction;
import com.miniproject.banking_system.model.TransactionType;
import com.miniproject.banking_system.repository.AccountRepository;
import com.miniproject.banking_system.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------------------------------------------------------
    // CREATE ACCOUNT
    // ---------------------------------------------------------
    @Test
    void testCreateAccount() {
        Account saved = new Account();
        saved.setId(1L);
        saved.setName("Shashank");
        saved.setBalance(0);

        when(accountRepository.save(any(Account.class))).thenReturn(saved);

        Account result = accountService.createAccount("Shashank", 0);

        assertEquals("Shashank", result.getName());
        assertEquals(0.0, result.getBalance());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    // ---------------------------------------------------------
    // GET ACCOUNT BY ID
    // ---------------------------------------------------------
    @Test
    void testGetAccount_Success() {
        Account acc = new Account();
        acc.setId(2L);
        acc.setName("Test User");

        when(accountRepository.findById(2L)).thenReturn(Optional.of(acc));

        Account result = accountService.getAccount(2L);
        assertEquals(2L, result.getId());
    }

    @Test
    void testGetAccount_NotFound() {
        when(accountRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> accountService.getAccount(5L));
    }

    // ---------------------------------------------------------
    // DEPOSIT
    // ---------------------------------------------------------
    @Test
    void testDeposit() {
        Account acc = new Account("User", 1000);
        acc.setId(1L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(acc));
        when(accountRepository.save(any(Account.class))).thenReturn(acc);

        Account result = accountService.deposit(1L, 500);

        assertEquals(1500.0, result.getBalance());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    // ---------------------------------------------------------
    // WITHDRAW
    // ---------------------------------------------------------
    @Test
    void testWithdraw_Success() {
        Account acc = new Account("User", 2000);
        acc.setId(1L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(acc));
        when(accountRepository.save(any(Account.class))).thenReturn(acc);

        Account result = accountService.withdraw(1L, 800);

        assertEquals(1200.0, result.getBalance());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testWithdraw_InsufficientFunds() {
        Account acc = new Account("User", 300);
        acc.setId(1L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(acc));

        assertThrows(InsufficientFundsException.class,
                () -> accountService.withdraw(1L, 500));
    }

    // ---------------------------------------------------------
    // TRANSFER FUNDS
    // ---------------------------------------------------------
    @Test
    void testTransferFunds_Success() {
        Account from = new Account("A", 5000);
        from.setId(1L);

        Account to = new Account("B", 2000);
        to.setId(2L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(from));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(to));

        String result = accountService.transferFunds(1L, 2L, 1000);

        assertEquals(4000.0, from.getBalance());
        assertEquals(3000.0, to.getBalance());
        verify(transactionRepository, times(2)).save(any(Transaction.class));
        assertTrue(result.contains("Transfer Successful"));
    }

    @Test
    void testTransferFunds_Insufficient() {
        Account from = new Account("A", 200);
        from.setId(1L);

        Account to = new Account("B", 500);
        to.setId(2L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(from));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(to));

        assertThrows(InsufficientFundsException.class,
                () -> accountService.transferFunds(1L, 2L, 500));
    }

    @Test
    void testTransferFunds_SameAccount() {
        assertThrows(IllegalArgumentException.class,
                () -> accountService.transferFunds(1L, 1L, 100));
    }

    // ---------------------------------------------------------
    // GET TRANSACTIONS
    // ---------------------------------------------------------
    @Test
    void testGetTransactions() {
        List<Transaction> txList = List.of(
                new Transaction(null, TransactionType.DEPOSIT, 500),
                new Transaction(null, TransactionType.WITHDRAW, 200)
        );

        when(transactionRepository.findByAccount_IdOrderByTimestampDesc(1L))
                .thenReturn(txList);

        List<Transaction> result = accountService.getTransactions(1L);

        assertEquals(2, result.size());
    }
}
