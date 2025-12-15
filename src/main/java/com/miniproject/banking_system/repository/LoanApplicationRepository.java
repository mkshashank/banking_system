package com.miniproject.banking_system.repository;

import com.miniproject.banking_system.model.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
}
