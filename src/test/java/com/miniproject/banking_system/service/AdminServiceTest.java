package com.miniproject.banking_system.service;

import com.miniproject.banking_system.dto.AdminDashboardResponse;
import com.miniproject.banking_system.dto.AdminSummaryResponse;
import com.miniproject.banking_system.model.Account;
import com.miniproject.banking_system.repository.AccountRepository;
import com.miniproject.banking_system.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- Test getTotalCustomers ----------
    @Test
    void shouldReturnTotalCustomersCorrectly() {
        when(accountRepository.getTotalCustomers()).thenReturn(10L);

        long result = adminService.getTotalCustomers();

        assertEquals(10L, result);
        verify(accountRepository, times(1)).getTotalCustomers();
    }

    // ---------- Test getTotalDeposits ----------
    @Test
    void shouldReturnTotalDepositsCorrectly() {
        when(transactionRepository.getTotalDeposits()).thenReturn(50000.0);

        double result = adminService.getTotalDeposits();

        assertEquals(50000.0, result);
        verify(transactionRepository, times(1)).getTotalDeposits();
    }

    // ---------- Test getTopAccounts ----------
    @Test
    void shouldReturnListOfTopAccounts() {
        List<Account> mockAccounts = List.of(
                new Account("Alice", 120000),
                new Account("Bob", 200000)
        );
        when(accountRepository.getTopAccounts()).thenReturn(mockAccounts);

        List<Account> result = adminService.getTopAccounts();

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
        verify(accountRepository, times(1)).getTopAccounts();
    }

    // ---------- Test getLoanSummary ----------
    @Test
    void shouldReturnCorrectLoanSummary() {
        when(accountRepository.getTotalCustomers()).thenReturn(5L);
        when(transactionRepository.getTotalDeposits()).thenReturn(100000.0);
        when(accountRepository.countTopAccounts()).thenReturn(2L);

        AdminSummaryResponse response = adminService.getLoanSummary();

        assertEquals(5, response.getTotalCustomers());
        assertEquals(100000.0, response.getTotalDeposits());
        assertEquals(2, response.getTopAccountsCount());
        assertEquals(8, response.getTotalLoanRequests());
        assertEquals(5, response.getEligibleLoans());
        assertEquals(3, response.getIneligibleLoans());
    }

    // ---------- Test getDashboardSummary ----------
    @Test
    void shouldReturnCompleteDashboardSummary() {
        when(accountRepository.getTotalCustomers()).thenReturn(6L);
        when(transactionRepository.getTotalDeposits()).thenReturn(82000.0);
        when(transactionRepository.getTotalWithdrawals()).thenReturn(25000.0);
        when(transactionRepository.getTotalTransactions()).thenReturn(15L);
        when(accountRepository.countTopAccounts()).thenReturn(2L);

        AdminDashboardResponse response = adminService.getDashboardSummary();

        assertEquals(6, response.getTotalCustomers());
        assertEquals(82000.0, response.getTotalDeposits());
        assertEquals(25000.0, response.getTotalWithdrawals());
        assertEquals(15, response.getTotalTransactions());
        assertEquals(2, response.getTopAccountsCount());
        assertEquals("ACTIVE", response.getSystemHealth());
        assertEquals(8, response.getTotalLoanRequests());
        assertEquals(5, response.getEligibleLoans());
        assertEquals(3, response.getIneligibleLoans());
    }

    // ---------- Test empty data ----------
    @Test
    void shouldHandleEmptyDataGracefully() {
        when(accountRepository.getTotalCustomers()).thenReturn(0L);
        when(transactionRepository.getTotalDeposits()).thenReturn(0.0);
        when(transactionRepository.getTotalWithdrawals()).thenReturn(0.0);
        when(transactionRepository.getTotalTransactions()).thenReturn(0L);
        when(accountRepository.countTopAccounts()).thenReturn(0L);

        AdminDashboardResponse response = adminService.getDashboardSummary();

        assertEquals(0, response.getTotalCustomers());
        assertEquals(0.0, response.getTotalDeposits());
        assertEquals(0.0, response.getTotalWithdrawals());
        assertEquals(0, response.getTotalTransactions());
        assertEquals(0, response.getTopAccountsCount());
        assertEquals("ACTIVE", response.getSystemHealth());
    }
}
