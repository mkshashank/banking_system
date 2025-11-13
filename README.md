---

# 🏦 Banking System – Spring Boot Project

A **complete Banking Management System** built using **Spring Boot**, designed to simulate real-world banking operations such as **account creation**, **balance inquiry**, **fund transfers**, **loan eligibility evaluation**, **interest & fixed deposit calculations**, **monthly statements**, and a **comprehensive admin analytics dashboard**.

This project follows a clean **layered architecture** — using DTOs, Services, Repositories, and Controllers — with robust exception handling and well-structured unit tests.

---

## ✨ Features

### 🏦 Account Management

* Create new bank accounts
* Fetch account details by account ID
* Deposit and withdraw funds with balance validation
* Perform fund transfers between accounts (atomic & transactional)
* View all transactions for a specific account
* List all existing accounts

### 💰 Loan Eligibility System

* Evaluate loan eligibility based on:

  * Age
  * Annual Income
  * Credit Score
  * Existing Loan Burden
* Dynamically calculate maximum eligible loan amount

### 📈 Financial Calculators

* **Simple Interest Calculator** → `/interest/calculateInterest`
* **Fixed Deposit Calculator** → `/fixedDeposit`
* Supports **compound interest** and **premature withdrawal penalty (1%)**

### 📅 Monthly Statement Generator

* Generate a monthly summary of all transactions for any month & year
* Calculates:

  * Opening Balance
  * Total Deposits
  * Total Withdrawals
  * Closing Balance

**Example Response:**

```json
{
  "month": "NOVEMBER",
  "openingBalance": 15000,
  "totalDeposits": 3000,
  "totalWithdrawals": 2000,
  "closingBalance": 16000
}
```

### 📊 Admin Dashboard

* Real-time analytics and reporting for admins using JPQL aggregation
* Includes:

  * Total customers
  * Total deposits and withdrawals
  * Top accounts (balance > ₹1L)
  * Loan eligibility summary (eligible vs ineligible)
  * Combined system dashboard → `/admin/dashboard`

**Example Response:**

```json
{
  "totalCustomers": 6,
  "totalDeposits": 82000.0,
  "totalWithdrawals": 25000.0,
  "totalTransactions": 15,
  "topAccountsCount": 2,
  "totalLoanRequests": 8,
  "eligibleLoans": 5,
  "ineligibleLoans": 3,
  "systemHealth": "ACTIVE"
}
```

---

## 🧩 Additional Features

✅ DTO-based data transfer
✅ Layered architecture (`Controller → Service → Repository → DB`)
✅ Custom exception handling (`AccountNotFoundException`, `InsufficientFundsException`, etc.)
✅ Transactional fund transfers (ensures atomic operations)
✅ SLF4J + Lombok-based structured logging
✅ Unit tests for major business logic (Loan, FD, Interest, Statement, Admin)
✅ Optimized JPQL aggregation for Admin Dashboard
✅ Modular and scalable design

---

## 📂 Project Structure

<details>
<summary>Click to view project structure</summary>

```
src/main/java/com/miniproject/banking_system
├── controller
│   ├── AccountController.java
│   ├── LoanEligibilityController.java
│   ├── FixedDepositController.java
│   ├── InterestController.java
│   ├── StatementController.java
│   └── AdminController.java
│
├── dto
│   ├── TransferRequest.java
│   ├── InterestRequest.java
│   ├── InterestResponse.java
│   ├── FixedDepositRequest.java
│   ├── FixedDepositResponse.java
│   ├── LoanEligibilityRequest.java
│   ├── LoanEligibilityResponse.java
│   ├── AdminSummaryResponse.java
│   └── AdminDashboardResponse.java
│
├── model
│   ├── Account.java
│   ├── Transaction.java
│   └── TransactionType.java
│
├── repository
│   ├── AccountRepository.java
│   └── TransactionRepository.java
│
├── service
│   ├── AccountService.java
│   ├── LoanEligibilityService.java
│   ├── FixedDepositService.java
│   ├── InterestService.java
│   ├── StatementService.java
│   └── AdminService.java
│
├── exception
│   ├── AccountNotFoundException.java
│   ├── InsufficientFundsException.java
│   └── GlobalExceptionHandler.java
│
└── resources
    └── application.properties
```

</details>

---

## 🚀 API Endpoints Summary

### 🏦 Account APIs

| Method | Endpoint                      | Description               |
| ------ | ----------------------------- | ------------------------- |
| POST   | `/accounts`                   | Create new account        |
| GET    | `/accounts/{id}`              | Get account details       |
| POST   | `/accounts/{id}/deposit`      | Deposit funds             |
| POST   | `/accounts/{id}/withdraw`     | Withdraw funds            |
| POST   | `/accounts/transfer`          | Transfer between accounts |
| GET    | `/accounts/transactions/{id}` | View all transactions     |
| GET    | `/accounts`                   | List all accounts         |

### 💰 Loan APIs

| Method | Endpoint           | Description               |
| ------ | ------------------ | ------------------------- |
| POST   | `/loanEligibility` | Evaluate loan eligibility |

### 📈 Financial Calculators

| Method | Endpoint                      | Description                      |
| ------ | ----------------------------- | -------------------------------- |
| POST   | `/interest/calculateInterest` | Calculate Simple Interest        |
| POST   | `/fixedDeposit`               | Calculate Fixed Deposit maturity |

### 📅 Monthly Statement

| Method | Endpoint                                    | Description                |
| ------ | ------------------------------------------- | -------------------------- |
| GET    | `/statement/{accountId}?month=MM&year=YYYY` | Generate monthly statement |

### 🧮 Admin APIs

| Method | Endpoint                | Description                      |
| ------ | ----------------------- | -------------------------------- |
| GET    | `/admin/totalCustomers` | Get total number of customers    |
| GET    | `/admin/totalDeposits`  | Get total deposits               |
| GET    | `/admin/topAccounts`    | Get top accounts (balance > ₹1L) |
| GET    | `/admin/loanSummary`    | Get loan summary                 |
| GET    | `/admin/dashboard`      | Get combined system dashboard    |

---

## ⚙️ Dependencies

* Java 17+
* Spring Boot 3+
* Spring Web
* Spring Data JPA
* Lombok
* MySQL / H2 Database
* JUnit 5
* Maven

---

## 🌐 Database Configuration

### 🧭 MySQL (default)

```properties
spring.application.name=banking_system
spring.datasource.url=jdbc:mysql://localhost:3306/accountdb
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### 🧪 H2 (In-Memory for Testing)

```properties
spring.datasource.url=jdbc:h2:mem:bankdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=update
```

---

## ▶️ How to Run

### 🧰 Using Maven

```bash
mvn clean install
mvn spring-boot:run
```

### 🖥️ From IDE (Eclipse / IntelliJ)

1. Import as **Maven Project**
2. Run `BankingSystemApplication.java`
3. Open browser → [http://localhost:8080](http://localhost:8080)

---

## 🧪 Testing

### Run All Unit Tests

```bash
mvn test
```

### Included Tests

* `InterestServiceTest`
* `FixedDepositServiceTest`
* `LoanEligibilityServiceTest`
* `StatementServiceTest`
* `AdminServiceTest`

---

## 🔮 Planned Enhancements

* 🔐 JWT-based authentication (Admin/User roles)
* 📄 Swagger / OpenAPI documentation
* 🕓 Scheduled interest calculation jobs
* ☁️ Docker & Cloud deployment (AWS/GCP)
* 📨 Event-driven notifications (Kafka)
* 💻 Frontend UI (React/Angular)

---

## 👨‍💻 Author

**Mk Shashank**
*Full Stack Java Developer | Passionate about scalable backend systems*

📧 Email: [mkshashanklcw@gmail.com](mailto:mkshashanklcw@gmail.com)
🌐 GitHub: [github.com/mkshashank](https://github.com/mkshashank)

---

⭐ *If you found this helpful, consider giving the repository a star!*

---
