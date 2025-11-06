package com.miniproject.banking_system.controller;

import com.miniproject.banking_system.dto.FixedDepositRequest;
import com.miniproject.banking_system.dto.FixedDepositResponse;
import com.miniproject.banking_system.service.FixedDepositService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fixedDeposit")
@Slf4j
public class FixedDepositController {

    @Autowired
    private FixedDepositService fixedDepositService;

    @PostMapping
    public FixedDepositResponse calculateFixedDeposit(@RequestBody FixedDepositRequest request) {
        log.info("Received Fixed Deposit request: {}", request);
        FixedDepositResponse response = fixedDepositService.calculateMaturity(request);
        log.info("Calculated Fixed Deposit response: {}", response);
        return response;
    }
}
