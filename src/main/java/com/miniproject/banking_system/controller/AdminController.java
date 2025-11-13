package com.miniproject.banking_system.controller;

import com.miniproject.banking_system.dto.AdminDashboardResponse;
import com.miniproject.banking_system.dto.AdminSummaryResponse;
import com.miniproject.banking_system.model.Account;
import com.miniproject.banking_system.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/totalCustomers")
    public long getTotalCustomers() {
        log.info("Fetching total customers");
        return adminService.getTotalCustomers();
    }

    @GetMapping("/totalDeposits")
    public double getTotalDeposits() {
        log.info("Fetching total deposits");
        return adminService.getTotalDeposits();
    }

    @GetMapping("/topAccounts")
    public List<Account> getTopAccounts() {
        log.info("Fetching top accounts with balance > ₹1L");
        return adminService.getTopAccounts();
    }

    @GetMapping("/loanSummary")
    public AdminSummaryResponse getLoanSummary() {
        log.info("Fetching loan summary");
        return adminService.getLoanSummary();
    }

    @GetMapping("/dashboard")
    public AdminDashboardResponse getDashboardSummary() {
        log.info("Fetching admin dashboard summary");
        return adminService.getDashboardSummary();
    }
}
