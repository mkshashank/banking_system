package com.miniproject.banking_system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double balance;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Transaction> transactions = new ArrayList<>();

    // ⭐ REQUIRED BY AccountService
    public Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
        this.createdAt = LocalDateTime.now();
    }
}
