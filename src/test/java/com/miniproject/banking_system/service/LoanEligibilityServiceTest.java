package com.miniproject.banking_system.service;

import com.miniproject.banking_system.dto.LoanEligibilityRequest;
import com.miniproject.banking_system.dto.LoanEligibilityResponse;
import com.miniproject.banking_system.model.Account;
import com.miniproject.banking_system.model.LoanApplication;
import com.miniproject.banking_system.repository.AccountRepository;
import com.miniproject.banking_system.repository.LoanApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LoanEligibilityServiceTest {

    private LoanEligibilityService service;

    private LoanApplicationRepository loanRepo;
    private AccountRepository accountRepo;

    @BeforeEach
    void setup() {
        loanRepo = Mockito.mock(LoanApplicationRepository.class);
        accountRepo = Mockito.mock(AccountRepository.class);

        // FIXED argument order
        service = new LoanEligibilityService(accountRepo, loanRepo);

        Account acc = new Account("Test User", 0);
        acc.setId(1L);

        Mockito.when(accountRepo.findById(1L)).thenReturn(Optional.of(acc));

        Mockito.when(loanRepo.save(Mockito.any(LoanApplication.class)))
                .thenAnswer(invocation -> {
                    LoanApplication app = invocation.getArgument(0);
                    app.setId(99L);
                    return app;
                });
    }


    @Test
    void shouldReturnEligible_whenAllConditionsSatisfied() {
        LoanEligibilityRequest request = new LoanEligibilityRequest();
        request.setAccountId(1L);
        request.setAge(25);
        request.setAnnualIncome(500000);
        request.setCreditScore(720);
        request.setExistingLoanAmount(100000);

        LoanEligibilityResponse response = service.evaluateEligibility(request);

        assertEquals("Eligible", response.getStatus());
        assertEquals("", response.getReason());
        assertEquals(500000 * 1.2 - 100000, response.getMaxLoanAmount());
    }

    @Test
    void shouldReject_whenAgeBelow21() {
        LoanEligibilityRequest request = new LoanEligibilityRequest();
        request.setAccountId(1L);
        request.setAge(19);
        request.setAnnualIncome(500000);
        request.setCreditScore(720);
        request.setExistingLoanAmount(50000);

        LoanEligibilityResponse response = service.evaluateEligibility(request);

        assertEquals("Not Eligible", response.getStatus());
        assertEquals("Minimum age requirement not met", response.getReason());
        assertEquals(0, response.getMaxLoanAmount());
    }

    @Test
    void shouldReject_whenIncomeLow() {
        LoanEligibilityRequest request = new LoanEligibilityRequest();
        request.setAccountId(1L);
        request.setAge(30);
        request.setAnnualIncome(250000);
        request.setCreditScore(710);
        request.setExistingLoanAmount(20000);

        LoanEligibilityResponse response = service.evaluateEligibility(request);

        assertEquals("Not Eligible", response.getStatus());
        assertEquals("Annual income below threshold", response.getReason());
        assertEquals(0, response.getMaxLoanAmount());
    }

    @Test
    void shouldReject_whenCreditScoreLow() {
        LoanEligibilityRequest request = new LoanEligibilityRequest();
        request.setAccountId(1L);
        request.setAge(28);
        request.setAnnualIncome(600000);
        request.setCreditScore(680);
        request.setExistingLoanAmount(50000);

        LoanEligibilityResponse response = service.evaluateEligibility(request);

        assertEquals("Not Eligible", response.getStatus());
        assertEquals("Credit score below minimum threshold", response.getReason());
        assertEquals(0, response.getMaxLoanAmount());
    }

    @Test
    void shouldReject_whenLoanToIncomeRatioTooHigh() {
        LoanEligibilityRequest request = new LoanEligibilityRequest();
        request.setAccountId(1L);
        request.setAge(32);
        request.setAnnualIncome(400000);
        request.setCreditScore(720);
        request.setExistingLoanAmount(200000);

        LoanEligibilityResponse response = service.evaluateEligibility(request);

        assertEquals("Not Eligible", response.getStatus());
        assertEquals("Loan-to-income ratio exceeds limit", response.getReason());
        assertEquals(0, response.getMaxLoanAmount());
    }
}
