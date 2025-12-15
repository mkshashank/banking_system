package com.miniproject.banking_system.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name = "credit_card_bill")
public class CreditCardBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Every bill belongs to an account
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private double totalSpending;
    private double paymentsMade;

    @Column(name = "due_date_value")
    private LocalDate dueDate;

    @Column(name = "current_date_value")
    private LocalDate currentDate;

    private double pendingAmount;
    private double interest;
    private double lateFee;
    private double totalDue;

    private String status;
    private int daysDelayed;
}
