package com.miniproject.banking_system.repository;

import com.miniproject.banking_system.model.ATMCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ATMCardRepository extends JpaRepository<ATMCard, Long> {
    Optional<ATMCard> findByCardNumber(String cardNumber);
}
