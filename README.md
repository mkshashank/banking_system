🏦 Banking System – Spring Boot Project

A complete Banking Management System built using Spring Boot, designed to simulate real-world banking operations such as account creation, balance inquiry, deposit/withdrawal, fund transfers, loan eligibility evaluation, interest & fixed deposit calculations, monthly statements, and admin analytics dashboard.

This project follows a clean layered architecture with DTOs, Services, Repositories, Controllers, and robust Exception Handling for reliability and maintainability.

✅ Features
🏦 Account Management

Create new bank accounts

Fetch account details by account ID

Deposit and withdraw funds with balance validation

Perform fund transfers between accounts (atomic transactions)

Check all transactions for an account

View all existing accounts

💰 Loan Eligibility System

Evaluate loan eligibility based on:

Age

Annual Income

Credit Score

Existing Loan Burden

Calculates maximum eligible loan amount dynamically

📊 Admin Dashboard

Real-time analytics for admins powered by JPQL aggregation:

Total customers

Total deposits and withdrawals

Top accounts (balance > ₹1L)

Loan summary (eligible vs ineligible)

Combined dashboard summary /admin/dashboard

📜 Transaction Management

Complete ledger-based transaction system

Logs every deposit, withdrawal, and transfer

View transaction history per account

Used as source data for monthly statements and reports

📅 Monthly Statement Generator

Generate a summary of transactions for any given month & year

Calculates:

Opening Balance

Total Deposits

Total Withdrawals

Closing Balance

Example Response:

{
  "month": "NOVEMBER",
  "openingBalance": 15000,
  "totalDeposits": 3000,
  "totalWithdrawals": 2000,
  "closingBalance": 16000
}

📈 Financial Calculators

Simple Interest Calculator → /interest/calculateInterest

Fixed Deposit Calculator → /fixedDeposit

Supports compound interest and premature withdrawal penalty (1%)

🛠️ Additional Features

✅ DTO-based data transfer

✅ Layered architecture (Controller → Service → Repository → DB)

✅ Custom exception handling (e.g., AccountNotFoundException, InsufficientFundsException)

✅ Transactional fund transfers (atomic operations)

✅ SLF4J + Lombok-based logging

✅ Unit tests for key business logic (Loan, FD, Interest)

✅ Optimized JPQL aggregation for Admin Dashboard

✅ Clean and modular design for scalability

📂 Project Structure
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

🚀 API Endpoints Summary
🏦 Account APIs
Method	Endpoint	Description
POST	/accounts	Create new account
GET	/accounts/{id}	Get account details
POST	/accounts/{id}/deposit	Deposit funds
POST	/accounts/{id}/withdraw	Withdraw funds
POST	/accounts/transfer	Transfer between accounts
GET	/accounts/transactions/{id}	View all transactions
GET	/accounts	List all accounts
💰 Loan APIs
Method	Endpoint	Description
POST	/loanEligibility	Evaluate loan eligibility
📈 Financial Calculators
Method	Endpoint	Description
POST	/interest/calculateInterest	Calculate Simple Interest
POST	/fixedDeposit	Calculate Fixed Deposit maturity
📅 Monthly Statement
Method	Endpoint	Description
GET	/statement/{accountId}?month=MM&year=YYYY	Generate monthly statement
🧮 Admin APIs
Method	Endpoint	Description
GET	/admin/totalCustomers	Get total number of customers
GET	/admin/totalDeposits	Get total deposits system-wide
GET	/admin/topAccounts	Get accounts with balance > ₹1L
GET	/admin/loanSummary	Get loan eligibility summary
GET	/admin/dashboard	Combined system dashboard

Example /admin/dashboard Response:

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

⚙️ Dependencies

Java 17+

Spring Boot 3+

Spring Web

Spring Data JPA

Lombok

MySQL / H2 Database

JUnit 5

Maven

🌐 Database Configuration
MySQL Example
spring.application.name=banking_system
spring.datasource.url=jdbc:mysql://localhost:3306/accountdb
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

H2 (In-Memory) for Testing
spring.datasource.url=jdbc:h2:mem:bankdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=update

▶️ How to Run
Using Maven
mvn clean install
mvn spring-boot:run

From IDE (Eclipse / IntelliJ)

Import as Maven Project

Run BankingSystemApplication.java

Server runs on:
👉 http://localhost:8080

🧪 Testing

Run all unit tests:

mvn test


Includes:

InterestServiceTest

FixedDepositServiceTest

LoanEligibilityServiceTest

🔮 Planned Enhancements

🔐 JWT-based authentication (Admin/User roles)

📄 Swagger / OpenAPI documentation

🕓 Scheduled interest calculation jobs

☁️ Docker & Cloud deployment (AWS/GCP)

📨 Event-driven notifications (Kafka)

💻 Frontend UI (React/Angular)

👨‍💻 Author

Mk Shashank
Full Stack Java Developer | Passionate about scalable backend systems
📧 mkshashanklcw@gmail.com

🌐 github.com/mkshashank

⭐ If you found this helpful, consider giving the repository a star!
