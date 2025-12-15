package com.miniproject.banking_system.service;

import com.miniproject.banking_system.dto.ATMResponse;
import com.miniproject.banking_system.dto.PinVerifyRequest;
import com.miniproject.banking_system.dto.ValidateCardRequest;
import com.miniproject.banking_system.dto.WithdrawRequest;
import com.miniproject.banking_system.exception.AccountNotFoundException;
import com.miniproject.banking_system.exception.InsufficientFundsException;
import com.miniproject.banking_system.model.ATMCard;
import com.miniproject.banking_system.model.Account;
import com.miniproject.banking_system.model.Transaction;
import com.miniproject.banking_system.model.TransactionType;
import com.miniproject.banking_system.repository.ATMCardRepository;
import com.miniproject.banking_system.repository.AccountRepository;
import com.miniproject.banking_system.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ATMService {

    private final ATMCardRepository atmCardRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    // -------------------- CARD VALIDATION --------------------
    public ATMResponse validateCard(ValidateCardRequest request) {

        ATMCard card = atmCardRepository.findByCardNumber(request.getCardNumber())
                .orElseThrow(() -> new IllegalArgumentException("Invalid card number"));

        // expiryDate is already LocalDate → just use it
        LocalDate expiry = card.getExpiryDate();
        if (expiry.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Card expired");
        }

        Account account = card.getAccount();
        if (account == null) {
            throw new AccountNotFoundException(null);
        }

        return new ATMResponse("CARD_VALID", account.getId(), account.getBalance());
    }

    // -------------------- PIN VERIFICATION --------------------
    public ATMResponse verifyPin(PinVerifyRequest request) {

        ATMCard card = atmCardRepository.findByCardNumber(request.getCardNumber())
                .orElseThrow(() -> new IllegalArgumentException("Invalid card"));

        if (!card.getPin().equals(request.getPin())) {
            card.setPinRetryCount(card.getPinRetryCount() + 1);
            atmCardRepository.save(card);

            if (card.getPinRetryCount() >= 3) {
                card.setActive(false);
                atmCardRepository.save(card);
                throw new IllegalArgumentException("Card blocked due to 3 incorrect attempts");
            }

            throw new IllegalArgumentException("Incorrect PIN");
        }

        // success → reset retry count
        card.setPinRetryCount(0);
        atmCardRepository.save(card);

        return new ATMResponse("PIN_VALID", card.getAccount().getId(), null);
    }

    // -------------------- WITHDRAWAL --------------------
    public ATMResponse withdraw(WithdrawRequest request) {

        ATMCard card = atmCardRepository.findByCardNumber(request.getCardNumber())
                .orElseThrow(() -> new IllegalArgumentException("Invalid card"));

        Account account = card.getAccount();
        if (account == null) throw new AccountNotFoundException(null);

        double amount = request.getAmount();

        if (amount <= 0) throw new IllegalArgumentException("Withdrawal amount must be positive");
        if (amount > account.getBalance()) throw new InsufficientFundsException(amount);

        // Daily limit check
        if (card.getWithdrawnToday() + amount > card.getDailyLimit()) {
            throw new IllegalArgumentException("Daily withdrawal limit exceeded");
        }

        // Deduct balance
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        // Update card daily usage
        card.setWithdrawnToday(card.getWithdrawnToday() + amount);
        atmCardRepository.save(card);

        // Save transaction
        Transaction tx = new Transaction(account, TransactionType.WITHDRAW, amount);
        transactionRepository.save(tx);

        return new ATMResponse("WITHDRAW_SUCCESS", account.getId(), account.getBalance());
    }
    public ATMResponse checkBalance(String cardNumber) {

        ATMCard card = atmCardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Invalid card"));

        Account account = accountRepository.findById(card.getAccount().getId())
                .orElseThrow(() -> new AccountNotFoundException(card.getAccount().getId()));

        return new ATMResponse("BALANCE", account.getId(), account.getBalance());
    }

}
