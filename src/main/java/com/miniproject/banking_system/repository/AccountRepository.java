package com.miniproject.banking_system.repository;

import com.miniproject.banking_system.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.SQLOutput;

public interface AccountRepository extends JpaRepository<Account, Long>
{

}