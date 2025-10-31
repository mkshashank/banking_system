package com.miniproject.banking_system.dto;

import lombok.Data;

@Data
public class InterestRequest {
    private double principal;
    private double rate;
    private double time;
}