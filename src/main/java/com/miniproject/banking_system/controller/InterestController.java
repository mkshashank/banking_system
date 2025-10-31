package com.miniproject.banking_system.controller;

import com.miniproject.banking_system.dto.InterestRequest;
import com.miniproject.banking_system.dto.InterestResponse;
import com.miniproject.banking_system.service.InterestService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/interest")
public class InterestController
{
    @Autowired
    InterestService interestService;

    @PostMapping("/calculateInterest")
    public InterestResponse calculateInterest(@Valid @RequestBody InterestRequest request)
    {
        log.info("Received interest calculation request: principal = {}, rate = {}, time = {}",
                request.getPrincipal(), request.getRate(), request.getTime());
        InterestResponse response = interestService.calculateInterest(request);
        log.info("Interest calculation result: interest = {}, totalAmount = {}",
                response.getInterest(), response.getTotalAmount());
        return response;
    }
}