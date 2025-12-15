package com.miniproject.banking_system.service;

import com.miniproject.banking_system.dto.CreditCardBillRequest;
import com.miniproject.banking_system.dto.CreditCardBillResponse;
import com.miniproject.banking_system.exception.AccountNotFoundException;
import com.miniproject.banking_system.model.Account;
import com.miniproject.banking_system.model.CreditCardBill;
import com.miniproject.banking_system.repository.AccountRepository;
import com.miniproject.banking_system.repository.CreditCardBillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditCardBillService {

    private final CreditCardBillRepository billRepo;
    private final AccountRepository accountRepo;

    private static final double APR = 0.36;
    private static final double DAILY_RATE = APR / 365.0;

    public CreditCardBillResponse calculateBill(CreditCardBillRequest req) {

        Account account = accountRepo.findById(req.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException(req.getAccountId()));

        LocalDate due = LocalDate.parse(req.getDueDate());
        LocalDate now = LocalDate.parse(req.getCurrentDate());

        double spending = req.getTotalSpending();
        double payments = req.getPaymentsMade();
        double pending = Math.max(spending - payments, 0);

        double lateFee = 0;
        double interest = 0;
        String status;
        int daysDelayed = 0;

        if (!now.isAfter(due)) {
            status = (pending == 0) ? "Paid On Time" : "Pending (Not Due)";
        } else {
            daysDelayed = (int) ChronoUnit.DAYS.between(due, now);

            if (pending == 0) {
                status = "Paid (Late)";
            } else {
                lateFee = computeLateFee(pending);
                interest = pending * DAILY_RATE * daysDelayed;
                status = "Overdue";
            }
        }

        lateFee = round(lateFee);
        interest = round(interest);
        double totalDue = round(pending + lateFee + interest);

        // Persist bill
        CreditCardBill bill = new CreditCardBill();
        bill.setAccount(account);
        bill.setTotalSpending(spending);
        bill.setPaymentsMade(payments);
        bill.setDueDate(due);
        bill.setCurrentDate(now);
        bill.setPendingAmount(pending);
        bill.setInterest(interest);
        bill.setLateFee(lateFee);
        bill.setTotalDue(totalDue);
        bill.setStatus(status);
        bill.setDaysDelayed(daysDelayed);

        billRepo.save(bill);

        return new CreditCardBillResponse(
                pending,
                interest,
                lateFee,
                totalDue,
                status,
                daysDelayed
        );
    }

    private double computeLateFee(double pending) {
        if (pending <= 500) return 0;
        if (pending <= 5000) return 500;
        return 750;
    }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
