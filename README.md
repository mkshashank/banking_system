# Banking System - Spring Boot Project

A complete **Banking Management System** built using **Spring Boot**, designed to simulate real-world banking operations including account creation, balance inquiry, deposit/withdrawal, fund transfer, loan eligibility evaluation, and transaction history tracking.

This project follows layered architecture with DTOs, Services, Repositories, Controllers, and proper exception handling.

---

## ✅ Features

### 🏦 **Account Management**

* Create new bank account
* Fetch account details by account number
* Deposit money
* Withdraw money with balance validation
* Check account balance
* Fund transfer between accounts (with validation)
* List all accounts

### 💰 **Loan Eligibility System**

* Evaluate loan eligibility based on:

  * Age
  * Annual income
  * Credit Score
  * Existing loan burden
* Calculates maximum eligible loan amount

### 📜 **Transaction Management**

* Track all deposit, withdrawal, and transfer transactions
* View transaction history for a specific account

### 🛠️ **Additional Features**

* Proper DTO layer
* Service & Repository architecture
* Custom Exception handling
* Logging via **SLF4J + Lombok**
* JUnit Test Cases for business logic

---

## 📂 Project Structure

```
src/main/java/com/miniproject/banking_system
├── controller
│   ├── AccountController.java
│   ├── LoanEligibilityController.java
│   └── TransactionController.java
├── dto
│   ├── AccountRequest.java
│   ├── AccountResponse.java
│   ├── DepositRequest.java
│   ├── TransferRequest.java
│   ├── LoanEligibilityRequest.java
│   └── LoanEligibilityResponse.java
├── model
│   ├── Account.java
│   └── Transaction.java
├── repository
│   ├── AccountRepository.java
│   └── TransactionRepository.java
└── service
    ├── AccountService.java
    ├── LoanEligibilityService.java
    └── TransactionService.java
```

---

## 🚀 API Endpoints Summary

### **Account APIs**

| Method | Endpoint                            | Description      |
| ------ | ----------------------------------- | ---------------- |
| POST   | `/accounts`                         | Create Account   |
| GET    | `/accounts/{accountNumber}`         | Get Account Info |
| POST   | `/accounts/deposit`                 | Deposit Amount   |
| POST   | `/accounts/withdraw`                | Withdraw Amount  |
| POST   | `/accounts/transfer`                | Transfer Funds   |
| GET    | `/accounts/balance/{accountNumber}` | Check Balance    |
| GET    | `/accounts`                         | Get All Accounts |

---

### **Loan APIs**

| Method | Endpoint           | Description            |
| ------ | ------------------ | ---------------------- |
| POST   | `/loanEligibility` | Check Loan Eligibility |

---

### **Transaction APIs**

| Method | Endpoint                        | Description                         |
| ------ | ------------------------------- | ----------------------------------- |
| GET    | `/transactions/{accountNumber}` | Get All Transactions for an Account |

---

## 📦 Dependencies

* Java 17+
* Spring Boot
* Spring Web
* Spring Data JPA
* Lombok
* H2 / MySQL (configurable)
* JUnit 5

---

## ▶️ How to Run

### **Using Maven**

```bash
mvn clean install
mvn spring-boot:run
```

### **From IDE** (IntelliJ / Eclipse / Spring Tool Suite)

* Import as Maven project
* Run `BankingSystemApplication.java`

---

## 🌐 Database Configuration

Uses **H2 in-memory DB** by default. For MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/banking_system
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

---

## 🧪 Test Coverage

* Unit tests for loan eligibility service
* More test cases planned for account and transfer logic

---

## 🔮 Future Enhancements

* JWT Authentication
* Role-based access (Admin/User)
* Swagger API Docs
* Scheduler for interest calculation
* Docker deployment
* React/Angular UI

---

## 👨‍💻 Author

**Mk Shashank**

> ⭐ If you found this helpful, star the repository on GitHub!

