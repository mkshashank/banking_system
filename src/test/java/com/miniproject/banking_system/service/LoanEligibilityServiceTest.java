package com.miniproject.banking_system.service;

import com.miniproject.banking_system.dto.LoanEligibilityRequest;
import com.miniproject.banking_system.dto.LoanEligibilityResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoanEligibilityServiceTest {

    private final LoanEligibilityService service = new LoanEligibilityService();

    @Test
    void shouldReturnEligible_whenAllConditionsSatisfied() {
        LoanEligibilityRequest request = new LoanEligibilityRequest();
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
        request.setAge(32);
        request.setAnnualIncome(400000);
        request.setCreditScore(720);
        request.setExistingLoanAmount(200000); // 0.5 ratio (>= 0.4)

        LoanEligibilityResponse response = service.evaluateEligibility(request);

        assertEquals("Not Eligible", response.getStatus());
        assertEquals("Loan-to-income ratio exceeds limit", response.getReason());
        assertEquals(0, response.getMaxLoanAmount());
    }
}
