package com.miniproject.banking_system.service;

import com.miniproject.banking_system.dto.CreditCardBillRequest;
import com.miniproject.banking_system.dto.CreditCardBillResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
public class CreditCardBillService {

    // APR chosen: 36% per year (as requested)
    private static final double APR = 0.36;
    private static final double DAILY_RATE = APR / 365.0;

    /**
     * Calculate credit card bill details using realistic bank logic:
     * - pendingAmount = totalSpending - paymentsMade
     * - if not past due date -> no interest, no late fee (unless paid)
     * - if past due and not fully paid -> late fee slab + interest per day delayed
     *
     * @param req CreditCardBillRequest
     * @return CreditCardBillResponse
     */
    public CreditCardBillResponse calculateBill(CreditCardBillRequest req) {
        validateRequest(req);

        LocalDate due;
        LocalDate now;
        try {
            due = LocalDate.parse(req.getDueDate());
            now = LocalDate.parse(req.getCurrentDate());
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid date format. Use ISO date: YYYY-MM-DD", ex);
        }

        double totalSpending = round(req.getTotalSpending());
        double paymentsMade = round(req.getPaymentsMade());

        if (paymentsMade < 0 || totalSpending < 0) {
            throw new IllegalArgumentException("Amounts must be non-negative");
        }

        // pending amount (what is left to be paid for this cycle)
        double pendingAmount = round(Math.max(0.0, totalSpending - paymentsMade));

        // If not past due date
        if (!now.isAfter(due)) {
            // If fully paid on or before due date
            if (paymentsMade >= totalSpending) {
                return new CreditCardBillResponse(0.0, 0.0, 0.0, 0.0, "Paid On Time", 0);
            } else {
                // Not yet due, but pending
                return new CreditCardBillResponse(pendingAmount, 0.0, 0.0, pendingAmount, "Pending (Not Due)", 0);
            }
        }

        // Past due date
        long daysDelayed = ChronoUnit.DAYS.between(due, now) > 0 ? (int) ChronoUnit.DAYS.between(due, now) : 0;

        // If payment after due date but fully paid now
        if (paymentsMade >= totalSpending) {
            // Paid late — banks sometimes may charge interest for days past due until payment date.
            // We'll consider this scenario as Paid (Late) with zero interest/late fee (simpler),
            // but you can opt to compute interest up to payment date if desired.
            return new CreditCardBillResponse(0.0, 0.0, 0.0, 0.0, "Paid (Late)", (int) daysDelayed);
        }

        // Overdue and not fully paid -> compute late fee + interest on pending amount for daysDelayed
        double lateFee = computeLateFee(pendingAmount);
        double interest = pendingAmount * DAILY_RATE * daysDelayed;

        // Round results
        lateFee = round(lateFee);
        interest = round(interest);

        double totalDue = round(pendingAmount + interest + lateFee);

        log.info("Credit bill calculated => pending={}, daysDelayed={}, interest={}, lateFee={}, totalDue={}",
                pendingAmount, daysDelayed, interest, lateFee, totalDue);

        return new CreditCardBillResponse(pendingAmount, interest, lateFee, totalDue, "Overdue", (int) daysDelayed);
    }

    private double computeLateFee(double pendingAmount) {
        // Slabs:
        // pendingAmount ≤ 500        → ₹0
        // 500 < pendingAmount ≤ 5000 → ₹500
        // > 5000                     → ₹750
        if (pendingAmount <= 500) return 0.0;
        if (pendingAmount <= 5000) return 500.0;
        return 750.0;
    }

    private void validateRequest(CreditCardBillRequest req) {
        if (req == null) throw new IllegalArgumentException("Request cannot be null");
        if (req.getDueDate() == null || req.getDueDate().isBlank())
            throw new IllegalArgumentException("dueDate is required (YYYY-MM-DD)");
        if (req.getCurrentDate() == null || req.getCurrentDate().isBlank())
            throw new IllegalArgumentException("currentDate is required (YYYY-MM-DD)");
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
