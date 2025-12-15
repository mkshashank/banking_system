package com.miniproject.banking_system.repository;

import com.miniproject.banking_system.model.InterestRecord;
import com.miniproject.banking_system.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestRecordRepository extends JpaRepository<InterestRecord, Long> {

    List<InterestRecord> findByAccount(Account account);
}
