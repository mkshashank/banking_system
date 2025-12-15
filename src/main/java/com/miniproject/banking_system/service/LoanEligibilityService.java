package com.miniproject.banking_system.service;

import com.miniproject.banking_system.dto.LoanEligibilityRequest;
import com.miniproject.banking_system.dto.LoanEligibilityResponse;
import com.miniproject.banking_system.exception.AccountNotFoundException;
import com.miniproject.banking_system.model.Account;
import com.miniproject.banking_system.model.LoanApplication;
import com.miniproject.banking_system.repository.AccountRepository;
import com.miniproject.banking_system.repository.LoanApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoanEligibilityService {

    private final AccountRepository accountRepository;
    private final LoanApplicationRepository loanApplicationRepository;

    public LoanEligibilityResponse evaluateEligibility(LoanEligibilityRequest request) {

        log.info("Evaluating loan eligibility for request: {}", request);

        // 🔥 New: fetch account for FK linking
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException(request.getAccountId()));

        int age = request.getAge();
        double income = request.getAnnualIncome();
        int creditScore = request.getCreditScore();
        double existingLoan = request.getExistingLoanAmount();

        String status;
        String reason = "";
        double maxLoanAmount = 0;

        if (age < 21) {
            status = "Not Eligible";
            reason = "Minimum age requirement not met";
        }
        else if (income <= 300000) {
            status = "Not Eligible";
            reason = "Annual income below threshold";
        }
        else if (creditScore < 700) {
            status = "Not Eligible";
            reason = "Credit score below minimum threshold";
        }
        else if ((existingLoan / income) >= 0.4) {
            status = "Not Eligible";
            reason = "Loan-to-income ratio exceeds limit";
        }
        else {
            status = "Eligible";
            maxLoanAmount = income * 1.2 - existingLoan;
        }

        // 🔥 New — save application record
        LoanApplication loan = new LoanApplication(
                account,
                age,
                income,
                creditScore,
                existingLoan,
                status,
                reason,
                maxLoanAmount
        );

        loan = loanApplicationRepository.save(loan);

        return new LoanEligibilityResponse(
                loan.getId(),      // NEW
                status,
                reason,
                maxLoanAmount
        );
    }
}
