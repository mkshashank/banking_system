package com.miniproject.banking_system.controller;

import com.miniproject.banking_system.dto.StatementResponse;
import com.miniproject.banking_system.service.StatementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/statement")
@Slf4j
public class StatementController {

    @Autowired
    private StatementService statementService;

    @GetMapping("/{accountId}")
    public StatementResponse getMonthlyStatement(
            @PathVariable Long accountId,
            @RequestParam int month,
            @RequestParam int year
    ) {
        log.info("Received monthly statement request for Account ID: {}, Month: {}, Year: {}",
                accountId, month, year);
        return statementService.generateMonthlyStatement(accountId, month, year);
    }
}
