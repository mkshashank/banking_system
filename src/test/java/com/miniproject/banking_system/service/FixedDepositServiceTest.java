package com.miniproject.banking_system.service;

import com.miniproject.banking_system.dto.FixedDepositRequest;
import com.miniproject.banking_system.dto.FixedDepositResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FixedDepositServiceTest {

    private final FixedDepositService service = new FixedDepositService();

    @Test
    void shouldCalculateMaturityCorrectly() {
        FixedDepositRequest request = new FixedDepositRequest();
        request.setAmount(50000);
        request.setRate(7);
        request.setTenure(3);
        request.setPrematureWithdrawal(false);

        FixedDepositResponse response = service.calculateMaturity(request);

        assertEquals("Full maturity", response.getMessage());
        assertEquals(61252, response.getMaturityAmount());
        assertEquals(11252, response.getInterestEarned());
    }

    @Test
    void shouldApplyPenaltyForPrematureWithdrawal() {
        FixedDepositRequest request = new FixedDepositRequest();
        request.setAmount(50000);
        request.setRate(7);
        request.setTenure(3);
        request.setPrematureWithdrawal(true);

        FixedDepositResponse response = service.calculateMaturity(request);

        assertEquals("Premature withdrawal applied (1% penalty)", response.getMessage());
        assertTrue(response.getMaturityAmount() < 61252);
    }

    @Test
    void shouldThrowExceptionForInvalidInput() {
        FixedDepositRequest request = new FixedDepositRequest();
        request.setAmount(-1000);
        request.setRate(7);
        request.setTenure(2);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> service.calculateMaturity(request));

        assertEquals("Amount, rate, and tenure must be positive values.", exception.getMessage());
    }
}
