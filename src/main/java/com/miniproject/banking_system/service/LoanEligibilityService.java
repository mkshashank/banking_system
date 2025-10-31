package com.miniproject.banking_system.service;

import com.miniproject.banking_system.dto.LoanEligibilityRequest;
import com.miniproject.banking_system.dto.LoanEligibilityResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoanEligibilityService {

    public LoanEligibilityResponse evaluateEligibility(LoanEligibilityRequest request) {
        log.info("Evaluating loan eligibility for request: {}", request);

        int age = request.getAge();
        double income = request.getAnnualIncome();
        int creditScore = request.getCreditScore();
        double existingLoan = request.getExistingLoanAmount();

        if (age < 21) {
            log.warn("Loan eligibility failed: Age {} is below minimum requirement", age);
            return new LoanEligibilityResponse("Not Eligible", "Minimum age requirement not met", 0);
        }

        if (income <= 300000) {
            log.warn("Loan eligibility failed: Annual income ₹{} is below threshold", income);
            return new LoanEligibilityResponse("Not Eligible", "Annual income below threshold", 0);
        }

        if (creditScore < 700) {
            log.warn("Loan eligibility failed: Credit score {} is below minimum threshold", creditScore);
            return new LoanEligibilityResponse("Not Eligible", "Credit score below minimum threshold", 0);
        }

        double loanToIncomeRatio = existingLoan / income;
        if (loanToIncomeRatio >= 0.4) {
            log.warn("Loan eligibility failed: Loan-to-income ratio {:.2f} exceeds limit", loanToIncomeRatio);
            return new LoanEligibilityResponse("Not Eligible", "Loan-to-income ratio exceeds limit", 0);
        }

        double maxLoanAmount = income * 1.2 - existingLoan;
        log.info("Loan eligibility passed. Max loan amount calculated: ₹{}", maxLoanAmount);
        return new LoanEligibilityResponse("Eligible", "", maxLoanAmount);
    }
}