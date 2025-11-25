package com.miniproject.banking_system.controller;

import com.miniproject.banking_system.dto.*;
import com.miniproject.banking_system.service.ATMService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/atm")
@RequiredArgsConstructor
public class ATMController {

    private final ATMService atmService;

    @PostMapping("/validateCard")
    public ATMResponse validateCard(@RequestBody ValidateCardRequest req) {
        return atmService.validateCard(req);
    }

    @PostMapping("/verifyPin")
    public ATMResponse verifyPin(@RequestBody PinVerifyRequest req) {
        return atmService.verifyPin(req);
    }

    @PostMapping("/withdraw")
    public ATMResponse withdraw(@RequestBody WithdrawRequest req) {
        return atmService.withdraw(req);
    }

    @GetMapping("/balance/{cardNumber}")
    public ATMResponse balance(@PathVariable String cardNumber) {
        return atmService.balanceInquiry(cardNumber);
    }
}
