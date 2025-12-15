package com.miniproject.banking_system.service;

import com.miniproject.banking_system.dto.FixedDepositRequest;
import com.miniproject.banking_system.dto.FixedDepositResponse;
import com.miniproject.banking_system.model.Account;
import com.miniproject.banking_system.model.FixedDeposit;
import com.miniproject.banking_system.repository.AccountRepository;
import com.miniproject.banking_system.repository.FixedDepositRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FixedDepositServiceTest {

    private FixedDepositService service;
    private FixedDepositRepository fdRepo;
    private AccountRepository accountRepo;

    @BeforeEach
    void setup() {
        fdRepo = Mockito.mock(FixedDepositRepository.class);
        accountRepo = Mockito.mock(AccountRepository.class);

        service = new FixedDepositService(fdRepo, accountRepo);

        Account acc = new Account("Test User", 100000);
        acc.setId(1L);

        Mockito.when(accountRepo.findById(1L)).thenReturn(Optional.of(acc));

        Mockito.when(fdRepo.save(Mockito.any(FixedDeposit.class))).thenAnswer(inv -> {
            FixedDeposit fd = inv.getArgument(0);
            fd.setId(100L);
            return fd;
        });
    }

    @Test
    void shouldCalculateMaturityCorrectly() {
        FixedDepositRequest request = new FixedDepositRequest();
        request.setAccountId(1L);
        request.setAmount(50000);
        request.setRate(7);
        request.setTenure(3);
        request.setPrematureWithdrawal(false);

        FixedDepositResponse response = service.calculateMaturity(request);

        assertEquals("Full maturity", response.getStatusMessage());
        assertEquals(61252, response.getMaturityAmount());
        assertEquals(11252, response.getInterestEarned());
        assertEquals(100L, response.getFdId());
    }

    @Test
    void shouldApplyPenaltyForPrematureWithdrawal() {
        FixedDepositRequest request = new FixedDepositRequest();
        request.setAccountId(1L);
        request.setAmount(50000);
        request.setRate(7);
        request.setTenure(3);
        request.setPrematureWithdrawal(true);

        FixedDepositResponse response = service.calculateMaturity(request);

        assertEquals("Premature withdrawal (1% penalty applied)", response.getStatusMessage());
        assertTrue(response.getMaturityAmount() < 61252);
    }

    @Test
    void shouldThrowExceptionForInvalidInput() {
        FixedDepositRequest request = new FixedDepositRequest();
        request.setAccountId(1L);
        request.setAmount(-1000);
        request.setRate(7);
        request.setTenure(2);

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> service.calculateMaturity(request));

        assertEquals("Amount, rate, and tenure must be positive.", ex.getMessage());
    }
}
