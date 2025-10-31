package com.miniproject.banking_system.controller;

import com.miniproject.banking_system.dto.LoanEligibilityRequest;
import com.miniproject.banking_system.dto.LoanEligibilityResponse;
import com.miniproject.banking_system.service.LoanEligibilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loanEligibility")
@Slf4j
public class LoanEligibilityController {

    @Autowired
    private LoanEligibilityService loanEligibilityService;

    @PostMapping
    public LoanEligibilityResponse checkEligibility(@RequestBody LoanEligibilityRequest request) {
        log.info("Received loan eligibility request: {}", request);
        LoanEligibilityResponse response = loanEligibilityService.evaluateEligibility(request);
        log.info("Eligibility result: {}", response);
        return response;
    }
}
