package com.miniproject.banking_system.repository;

import com.miniproject.banking_system.model.FixedDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FixedDepositRepository extends JpaRepository<FixedDeposit, Long> {
}
