package com.miniproject.banking_system.service;

import com.miniproject.banking_system.dto.InterestRequest;
import com.miniproject.banking_system.dto.InterestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InterestService {

    public InterestResponse calculateInterest(InterestRequest request) {
        log.info("Starting interest calculation...");
        log.debug("Received InterestRequest: principal={}, rate={}, time={}",
                request.getPrincipal(), request.getRate(), request.getTime());

        double principal = request.getPrincipal();
        double rate = request.getRate();
        double time = request.getTime();

        // Validate input
        if (principal <= 0 || rate <= 0 || time <= 0) {
            log.warn("Invalid input detected: principal={}, rate={}, time={}", principal, rate, time);
            throw new IllegalArgumentException("Principal, rate, and time must be positive numbers.");
        }

        // Calculate interest
        double interest = (principal * rate * time) / 100;
        double totalAmount = principal + interest;

        log.info("Interest calculation successful.");
        log.debug("Calculated interest={}, totalAmount={}", interest, totalAmount);

        return new InterestResponse(interest, totalAmount);
    }
}