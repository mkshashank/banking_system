package com.miniproject.banking_system.controller;

import com.miniproject.banking_system.dto.ATMResponse;
import com.miniproject.banking_system.dto.PinVerifyRequest;
import com.miniproject.banking_system.dto.ValidateCardRequest;
import com.miniproject.banking_system.dto.WithdrawRequest;
import com.miniproject.banking_system.service.ATMService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/atm")
@RequiredArgsConstructor
public class ATMController {

    private final ATMService atmService;

    @PostMapping("/validateCard")
    public ATMResponse validateCard(@RequestBody ValidateCardRequest request) {
        return atmService.validateCard(request);
    }

    @PostMapping("/verifyPin")
    public ATMResponse verifyPin(@RequestBody PinVerifyRequest request) {
        return atmService.verifyPin(request);
    }

    @PostMapping("/withdraw")
    public ATMResponse withdraw(@RequestBody WithdrawRequest request) {
        return atmService.withdraw(request);
    }

    @GetMapping("/balance/{cardNumber}")
    public ATMResponse checkBalance(@PathVariable String cardNumber) {
        return atmService.checkBalance(cardNumber);
    }

}
