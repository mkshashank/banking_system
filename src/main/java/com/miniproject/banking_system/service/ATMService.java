package com.miniproject.banking_system.service;

import com.miniproject.banking_system.dto.ATMResponse;
import com.miniproject.banking_system.dto.PinVerifyRequest;
import com.miniproject.banking_system.dto.ValidateCardRequest;
import com.miniproject.banking_system.dto.WithdrawRequest;
import com.miniproject.banking_system.model.ATMCard;
import com.miniproject.banking_system.repository.ATMCardRepository;
import com.miniproject.banking_system.exception.AccountNotFoundException;
import com.miniproject.banking_system.model.Account;
import com.miniproject.banking_system.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ATMService {

    private final ATMCardRepository cardRepo;
    private final AccountRepository accountRepo;

    // 1) Validate Card
    public ATMResponse validateCard(ValidateCardRequest req) {
        ATMCard card = cardRepo.findByCardNumber(req.getCardNumber())
                .orElseThrow(() -> new IllegalArgumentException("Invalid card"));

        if (!card.isActive())
            return new ATMResponse("Card Blocked", 0);

        // Check expiry
        LocalDate expiry = LocalDate.parse(card.getExpiryDate() + "-01");
        if (expiry.isBefore(LocalDate.now()))
            return new ATMResponse("Card Expired", 0);

        return new ATMResponse("Card Validated", 0);
    }

    // 2) Verify PIN
    public ATMResponse verifyPin(PinVerifyRequest req) {
        ATMCard card = cardRepo.findByCardNumber(req.getCardNumber())
                .orElseThrow(() -> new IllegalArgumentException("Invalid card"));

        if (!card.isActive())
            return new ATMResponse("Card Blocked", 0);

        if (!card.getPin().equals(req.getPin())) {
            card.setPinRetryCount(card.getPinRetryCount() + 1);

            if (card.getPinRetryCount() >= 3) {
                card.setActive(false);
                cardRepo.save(card);
                return new ATMResponse("Card Blocked - 3 Wrong Attempts", 0);
            }

            cardRepo.save(card);
            return new ATMResponse("Incorrect PIN", 0);
        }

        // Correct PIN
        card.setPinRetryCount(0);
        cardRepo.save(card);

        Account acc = getAccount(card);
        return new ATMResponse("PIN Verified", acc.getBalance());
    }

    // 3) Withdraw money with validations
    public ATMResponse withdraw(WithdrawRequest req) {
        ATMCard card = cardRepo.findByCardNumber(req.getCardNumber())
                .orElseThrow(() -> new IllegalArgumentException("Invalid card"));

        if (!card.isActive())
            return new ATMResponse("Card Blocked", 0);

        Account acc = getAccount(card);

        // Daily limit check
        if (card.getWithdrawnToday() + req.getAmount() > card.getDailyLimit())
            return new ATMResponse("Daily limit exceeded", acc.getBalance());

        // Balance check
        if (acc.getBalance() < req.getAmount())
            return new ATMResponse("Insufficient Balance", acc.getBalance());

        // Perform withdrawal
        acc.setBalance(acc.getBalance() - req.getAmount());
        card.setWithdrawnToday(card.getWithdrawnToday() + req.getAmount());

        accountRepo.save(acc);
        cardRepo.save(card);

        return new ATMResponse("Withdrawal Successful", acc.getBalance());
    }

    // 4) Balance Inquiry
    public ATMResponse balanceInquiry(String cardNumber) {
        ATMCard card = cardRepo.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Invalid card"));

        Account acc = getAccount(card);
        return new ATMResponse("Balance Retrieved", acc.getBalance());
    }

    private Account getAccount(ATMCard card) {
        return accountRepo.findById(card.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException(card.getAccountId()));
    }
}
