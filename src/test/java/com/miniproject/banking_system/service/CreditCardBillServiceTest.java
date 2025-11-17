package com.miniproject.banking_system.service;

import com.miniproject.banking_system.dto.CreditCardBillRequest;
import com.miniproject.banking_system.dto.CreditCardBillResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreditCardBillServiceTest {

    private CreditCardBillService service;

    @BeforeEach
    void setup() {
        service = new CreditCardBillService();
    }

    @Test
    void testPaidOnTime() {
        CreditCardBillRequest req = new CreditCardBillRequest();
        req.setTotalSpending(10000);
        req.setPaymentsMade(10000);
        req.setDueDate("2025-11-30");
        req.setCurrentDate("2025-11-25"); // before due

        CreditCardBillResponse resp = service.calculateBill(req);

        assertEquals(0.0, resp.getPendingAmount());
        assertEquals(0.0, resp.getInterest());
        assertEquals(0.0, resp.getLateFee());
        assertEquals(0.0, resp.getTotalDue());
        assertEquals("Paid On Time", resp.getStatus());
    }

    @Test
    void testPendingNotDue() {
        CreditCardBillRequest req = new CreditCardBillRequest();
        req.setTotalSpending(5000);
        req.setPaymentsMade(2000);
        req.setDueDate("2025-11-30");
        req.setCurrentDate("2025-11-28"); // before due

        CreditCardBillResponse resp = service.calculateBill(req);

        assertEquals(3000.0, resp.getPendingAmount());
        assertEquals("Pending (Not Due)", resp.getStatus());
        assertEquals(0.0, resp.getInterest());
        assertEquals(0.0, resp.getLateFee());
        assertEquals(3000.0, resp.getTotalDue());
    }

    @Test
    void testOverdueSmallAmount_fewDays() {
        // pending = 2000, due on Nov 30, now Dec 5 => 5 days delayed
        CreditCardBillRequest req = new CreditCardBillRequest();
        req.setTotalSpending(4000);
        req.setPaymentsMade(2000);
        req.setDueDate("2025-11-30");
        req.setCurrentDate("2025-12-05"); // 5 days late

        CreditCardBillResponse resp = service.calculateBill(req);

        assertEquals(2000.0, resp.getPendingAmount());
        // lateFee for 2000 -> slab ₹500
        assertEquals(500.0, resp.getLateFee());
        // interest = 2000 * (0.36/365) * 5
        double expectedInterest = Math.round((2000.0 * (0.36/365.0) * 5) * 100.0) / 100.0;
        assertEquals(expectedInterest, resp.getInterest());
        assertEquals("Overdue", resp.getStatus());
        assertEquals(5, resp.getDaysDelayed());
        double expectedTotal = Math.round((2000.0 + expectedInterest + 500.0) * 100.0) / 100.0;
        assertEquals(expectedTotal, resp.getTotalDue());
    }

    @Test
    void testOverdueLargeAmount_manyDays() {
        // pending 20000, delayed 30 days
        CreditCardBillRequest req = new CreditCardBillRequest();
        req.setTotalSpending(30000);
        req.setPaymentsMade(10000);
        req.setDueDate("2025-10-01");
        req.setCurrentDate("2025-10-31"); // 30 days late

        CreditCardBillResponse resp = service.calculateBill(req);

        assertEquals(20000.0, resp.getPendingAmount());
        // late fee for >5000 => 750
        assertEquals(750.0, resp.getLateFee());
        double expectedInterest = Math.round((20000.0 * (0.36/365.0) * 30) * 100.0) / 100.0;
        assertEquals(expectedInterest, resp.getInterest());
        assertEquals("Overdue", resp.getStatus());
    }

    @Test
    void testZeroPending() {
        CreditCardBillRequest req = new CreditCardBillRequest();
        req.setTotalSpending(0);
        req.setPaymentsMade(0);
        req.setDueDate("2025-11-30");
        req.setCurrentDate("2025-12-01"); // after due but nothing pending

        CreditCardBillResponse resp = service.calculateBill(req);

        assertEquals(0.0, resp.getPendingAmount());
        assertEquals(0.0, resp.getInterest());
        assertEquals(0.0, resp.getLateFee());
        assertEquals(0.0, resp.getTotalDue());
        // If nothing pending and after due, we treat as Paid (Late) or Paid On Time? Here it's Paid (Late)
        assertEquals("Paid (Late)", resp.getStatus());
    }
}
