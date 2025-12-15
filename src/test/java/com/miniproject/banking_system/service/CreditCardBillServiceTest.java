package com.miniproject.banking_system.service;

import com.miniproject.banking_system.dto.CreditCardBillRequest;
import com.miniproject.banking_system.dto.CreditCardBillRequest;
import com.miniproject.banking_system.dto.CreditCardBillResponse;
import com.miniproject.banking_system.repository.AccountRepository;
import com.miniproject.banking_system.repository.CreditCardBillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class CreditCardBillServiceTest {

    private CreditCardBillService service;

    private CreditCardBillRepository billRepo;
    private AccountRepository accountRepo;

    @BeforeEach
    void setup() {
        billRepo = Mockito.mock(CreditCardBillRepository.class);
        accountRepo = Mockito.mock(AccountRepository.class);

        service = new CreditCardBillService(billRepo, accountRepo);
    }

    @Test
    void testPaidOnTime() {
        CreditCardBillRequest req = new CreditCardBillRequest();
        req.setTotalSpending(10000);
        req.setPaymentsMade(10000);
        req.setDueDate("2025-11-30");
        req.setCurrentDate("2025-11-25");

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
        req.setCurrentDate("2025-11-28");

        CreditCardBillResponse resp = service.calculateBill(req);

        assertEquals(3000.0, resp.getPendingAmount());
        assertEquals("Pending (Not Due)", resp.getStatus());
        assertEquals(0.0, resp.getInterest());
        assertEquals(0.0, resp.getLateFee());
        assertEquals(3000.0, resp.getTotalDue());
    }

    @Test
    void testOverdueSmallAmount_fewDays() {
        CreditCardBillRequest req = new CreditCardBillRequest();
        req.setTotalSpending(4000);
        req.setPaymentsMade(2000);
        req.setDueDate("2025-11-30");
        req.setCurrentDate("2025-12-05");

        CreditCardBillResponse resp = service.calculateBill(req);

        assertEquals(2000.0, resp.getPendingAmount());
        assertEquals(500.0, resp.getLateFee());

        double expectedInterest =
                Math.round((2000.0 * (0.36 / 365.0) * 5) * 100.0) / 100.0;

        assertEquals(expectedInterest, resp.getInterest());
        assertEquals("Overdue", resp.getStatus());
        assertEquals(5, resp.getDaysDelayed());

        double expectedTotal =
                Math.round((2000.0 + expectedInterest + 500.0) * 100.0) / 100.0;

        assertEquals(expectedTotal, resp.getTotalDue());
    }

    @Test
    void testOverdueLargeAmount_manyDays() {
        CreditCardBillRequest req = new CreditCardBillRequest();
        req.setTotalSpending(30000);
        req.setPaymentsMade(10000);
        req.setDueDate("2025-10-01");
        req.setCurrentDate("2025-10-31");

        CreditCardBillResponse resp = service.calculateBill(req);

        assertEquals(20000.0, resp.getPendingAmount());
        assertEquals(750.0, resp.getLateFee());

        double expectedInterest =
                Math.round((20000.0 * (0.36 / 365.0) * 30) * 100.0) / 100.0;

        assertEquals(expectedInterest, resp.getInterest());
        assertEquals("Overdue", resp.getStatus());
    }

    @Test
    void testZeroPending() {
        CreditCardBillRequest req = new CreditCardBillRequest();
        req.setTotalSpending(0);
        req.setPaymentsMade(0);
        req.setDueDate("2025-11-30");
        req.setCurrentDate("2025-12-01");

        CreditCardBillResponse resp = service.calculateBill(req);

        assertEquals(0.0, resp.getPendingAmount());
        assertEquals(0.0, resp.getInterest());
        assertEquals(0.0, resp.getLateFee());
        assertEquals(0.0, resp.getTotalDue());
        assertEquals("Paid (Late)", resp.getStatus());
    }
}
