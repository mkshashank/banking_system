package com.miniproject.banking_system.controller;

import com.miniproject.banking_system.dto.CreditCardBillRequest;
import com.miniproject.banking_system.dto.CreditCardBillResponse;
import com.miniproject.banking_system.service.CreditCardBillService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/creditCardBill")
@Slf4j
public class CreditCardBillController {

    @Autowired
    private CreditCardBillService creditCardBillService;

    @PostMapping
    public CreditCardBillResponse calculateBill(@Valid @RequestBody CreditCardBillRequest request) {
        log.info("Received credit card bill request: {}", request);
        CreditCardBillResponse response = creditCardBillService.calculateBill(request);
        log.info("Credit card bill response: {}", response);
        return response;
    }
}
