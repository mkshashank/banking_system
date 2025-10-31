package com.miniproject.banking_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankingSystemApplication {

	public static void main(String[] args)
    {
        System.out.println("main() starts => ");
		SpringApplication.run(BankingSystemApplication.class, args);
        System.out.println("main() ends => ");
	}

}
