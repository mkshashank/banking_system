package com.miniproject.banking_system.service;

import com.miniproject.banking_system.dto.FixedDepositRequest;
import com.miniproject.banking_system.dto.FixedDepositResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FixedDepositService {

    public FixedDepositResponse calculateMaturity(FixedDepositRequest request) {
        double principal = request.getAmount();
        double rate = request.getRate();
        int years = request.getTenure();
        boolean premature = request.isPrematureWithdrawal();

        // ✅ Input validation
        if (principal <= 0 || rate <= 0 || years <= 0) {
            log.warn("Invalid input: amount={}, rate={}, tenure={}", principal, rate, years);
            throw new IllegalArgumentException("Amount, rate, and tenure must be positive values.");
        }

        log.info("Calculating fixed deposit maturity: amount={}, rate={}, tenure={}, premature={}",
                principal, rate, years, premature);

        // Compound Interest formula: A = P * (1 + r/100)^t
        double maturityAmount = principal * Math.pow((1 + rate / 100), years);
        double interestEarned = maturityAmount - principal;

        // Apply 1% penalty if premature withdrawal is true
        if (premature) {
            double penalty = maturityAmount * 0.01;
            maturityAmount -= penalty;
            interestEarned = maturityAmount - principal;
            log.info("Premature withdrawal: penalty={}, adjusted maturityAmount={}", penalty, maturityAmount);
        }

        log.info("Fixed Deposit maturity calculated successfully.");
        return new FixedDepositResponse(
                Math.round(maturityAmount),
                Math.round(interestEarned),
                premature ? "Premature withdrawal applied (1% penalty)" : "Full maturity"
        );
    }
}
