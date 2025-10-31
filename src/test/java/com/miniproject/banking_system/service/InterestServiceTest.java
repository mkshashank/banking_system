package com.miniproject.banking_system.service;

import com.miniproject.banking_system.dto.InterestRequest;
import com.miniproject.banking_system.dto.InterestResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InterestServiceTest {

    private final InterestService interestService = new InterestService();

    @Test
    void testCalculateInterest_basicCase() {
        InterestRequest request = new InterestRequest();
        request.setPrincipal(10000);
        request.setRate(6.5);
        request.setTime(2);

        InterestResponse response = interestService.calculateInterest(request);

        assertEquals(1300.0, response.getInterest());
        assertEquals(11300.0, response.getTotalAmount());
    }

    @Test
    void testCalculateInterest_differentRateAndTime() {
        InterestRequest request = new InterestRequest();
        request.setPrincipal(5000);
        request.setRate(5);
        request.setTime(3);

        InterestResponse response = interestService.calculateInterest(request);

        assertEquals(750.0, response.getInterest());
        assertEquals(5750.0, response.getTotalAmount());
    }

    @Test
    void testCalculateInterest_highValues() {
        InterestRequest request = new InterestRequest();
        request.setPrincipal(100000);
        request.setRate(10);
        request.setTime(5);

        InterestResponse response = interestService.calculateInterest(request);

        assertEquals(50000.0, response.getInterest());
        assertEquals(150000.0, response.getTotalAmount());
    }

    @Test
    void testCalculateInterest_invalidInput_zeroPrincipal() {
        InterestRequest request = new InterestRequest();
        request.setPrincipal(0);
        request.setRate(5);
        request.setTime(2);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                interestService.calculateInterest(request));

        assertEquals("Principal, rate, and time must be positive numbers.", exception.getMessage());
    }

    @Test
    void testCalculateInterest_invalidInput_negativeRate() {
        InterestRequest request = new InterestRequest();
        request.setPrincipal(1000);
        request.setRate(-3);
        request.setTime(1);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                interestService.calculateInterest(request));

        assertEquals("Principal, rate, and time must be positive numbers.", exception.getMessage());
    }
}
