package com.miniproject.banking_system.service;

import com.miniproject.banking_system.dto.FixedDepositRequest;
import com.miniproject.banking_system.dto.FixedDepositResponse;
import com.miniproject.banking_system.exception.AccountNotFoundException;
import com.miniproject.banking_system.model.Account;
import com.miniproject.banking_system.model.FixedDeposit;
import com.miniproject.banking_system.repository.AccountRepository;
import com.miniproject.banking_system.repository.FixedDepositRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FixedDepositService {

    private final FixedDepositRepository fdRepo;
    private final AccountRepository accountRepo;

    public FixedDepositResponse calculateMaturity(FixedDepositRequest request) {

        Account account = accountRepo.findById(request.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException(request.getAccountId()));

        double principal = request.getAmount();
        double rate = request.getRate();
        int years = request.getTenure();
        boolean premature = request.isPrematureWithdrawal();

        if (principal <= 0 || rate <= 0 || years <= 0) {
            throw new IllegalArgumentException("Amount, rate, and tenure must be positive.");
        }

        double maturityAmount = principal * Math.pow((1 + rate / 100), years);
        double interestEarned = maturityAmount - principal;

        if (premature) {
            double penalty = maturityAmount * 0.01;
            maturityAmount -= penalty;
            interestEarned = maturityAmount - principal;
        }

        // ⭐ Save the FD record
        FixedDeposit fd = new FixedDeposit();
        fd.setAccount(account);
        fd.setAmount(principal);
        fd.setRate(rate);
        fd.setTenure(years);
        fd.setPrematureWithdrawal(premature);
        fd.setMaturityAmount(maturityAmount);
        fd.setInterestEarned(interestEarned);
        fd.setStatusMessage(
                premature ? "Premature withdrawal (1% penalty applied)" : "Full maturity"
        );

        fd = fdRepo.save(fd);

        return new FixedDepositResponse(
                fd.getId(),
                maturityAmount,
                interestEarned,
                fd.getStatusMessage()
        );
    }
}
