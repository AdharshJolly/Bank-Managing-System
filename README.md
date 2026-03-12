# GlobalDigitalBank

[![Build](https://github.com/AdharshJolly/Bank-Managing-System/actions/workflows/build.yml/badge.svg)](https://github.com/AdharshJolly/Bank-Managing-System/actions/workflows/build.yml)
[![Java](https://img.shields.io/badge/Java-17%2B-blue?logo=openjdk)](https://openjdk.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

A Java console-based digital banking application with a layered MVC-like architecture, dual-portal design, and role-based access control.

---

## Features

### Account Management

- Create **Savings** (personal) and **Current** (business) accounts with full validation
- **Privilege tiers** (Platinum / Gold / Silver / Bronze) controlling interest rates and overdraft limits
- Deposit, withdraw, and transfer between accounts with transaction recording
- Account lifecycle: activate and close accounts
- Interest calculation and application for Savings accounts (individual and batch)
- Overdraft support for Current accounts based on privilege tier
- Minimum opening balance of 500.00 for Savings accounts
- **Daily withdrawal limit** of 50,000 per account (resets each calendar day)
- **Privilege upgrade/downgrade** by Manager or Admin

### Security & Access Control

- **Dual portal** architecture: Employee Portal (branch terminal) and Customer Self-Service Portal
- **Role-based access control** (RBAC) per `EmployeeRole`: TELLER, MANAGER, ADMIN
- **PIN lockout** after 3 failed attempts — for both customers and employees
- Customer and employee unlock operations (MANAGER+ for customers, ADMIN for employees)
- **Employee management** (ADMIN only): create employees, deactivate employees, list all staff
- Deactivated employees cannot log in

### Audit & History

- Full **transaction history** per account (deposit, withdrawal, transfer debit/credit, interest)
- **Audit log** recording every employee write action with employee ID, name, action, target account, and timestamp (ADMIN viewable)

---

## Project Structure

```
src/
├── App.java                                  ← Entry point / dual-portal interactive menu
└── com/talenciaglobal/gdb/
    ├── model/
    │   ├── User.java                         ← Interface: customer identity + PIN auth
    │   ├── Employee.java                     ← Interface: bank staff identity + role
    │   ├── EmployeeRole.java                 ← Enum: TELLER, MANAGER, ADMIN (with RBAC methods)
    │   ├── BankEmployee.java                 ← Implements Employee; PIN lockout; isActive flag
    │   ├── Account.java                      ← Implements User; base class (deposit, withdraw, lifecycle, daily limit)
    │   ├── SavingsAccount.java               ← Personal accounts (calculateInterest, DOB, phone)
    │   ├── CurrentAccount.java               ← Business accounts (overdraft limit)
    │   ├── Privilege.java                    ← Tier enum with interest rate + overdraft limit
    │   ├── Transaction.java                  ← Immutable transaction record
    │   ├── TransactionType.java              ← Enum: DEPOSIT, WITHDRAWAL, TRANSFER_DEBIT/CREDIT, INTEREST
    │   └── AuditEntry.java                   ← Immutable audit record (employeeId, action, timestamp)
    ├── repository/
    │   ├── AccountRepository.java            ← In-memory HashMap store (CRUD + findByName)
    │   ├── EmployeeRepository.java           ← In-memory HashMap store for BankEmployee
    │   └── AuditLogRepository.java           ← Append-only audit log (findAll, findByEmployee)
    └── controller/
        └── AccountController.java            ← All operations, session management, RBAC enforcement
```

---

## Privilege Tiers

| Tier     | Savings Interest Rate | Overdraft Limit (Current Accounts) |
| -------- | --------------------- | ---------------------------------- |
| Platinum | 5.0%                  | 50,000                             |
| Gold     | 4.0%                  | 30,000                             |
| Silver   | 3.0%                  | 10,000                             |
| Bronze   | 2.0%                  | 0                                  |

---

## Role Permissions

| Operation                        | TELLER | MANAGER | ADMIN |
| -------------------------------- | :----: | :-----: | :---: |
| Deposit / Withdraw / Transfer    |   ✓    |    ✓    |   ✓   |
| View Account / Transaction Hist. |   ✓    |    ✓    |   ✓   |
| Search Customer by Name          |   ✓    |    ✓    |   ✓   |
| Create / Activate / Close Acct.  |        |    ✓    |   ✓   |
| List All Accounts                |        |    ✓    |   ✓   |
| Apply Interest (single / batch)  |        |    ✓    |   ✓   |
| Unlock Customer Account          |        |    ✓    |   ✓   |
| Change Account Privilege         |        |    ✓    |   ✓   |
| Unlock Employee Account          |        |         |   ✓   |
| Create / Deactivate Employee     |        |         |   ✓   |
| View Audit Log                   |        |         |   ✓   |

---

## Getting Started

### Prerequisites

- Java 17 or higher
- VS Code with the [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)

### Run

**Using VS Code:**
Open `src/App.java` and click **Run**.

**Using terminal (from project root):**

```bash
javac -d bin src/com/talenciaglobal/gdb/model/*.java \
             src/com/talenciaglobal/gdb/repository/*.java \
             src/com/talenciaglobal/gdb/controller/*.java \
             src/App.java

java -cp bin App
```

---

## Portal Design

### Employee Portal (Branch Terminal)

Employee logs in with their **Employee ID** (e.g. `EMP0001`) and 4-digit PIN.  
Menu options are dynamically shown based on the employee's role.

**Pre-seeded staff (for testing):**

| Employee ID | Name          | Role    | PIN  |
| ----------- | ------------- | ------- | ---- |
| EMP0001     | Alice Admin   | ADMIN   | 1111 |
| EMP0002     | Bob Teller    | TELLER  | 2222 |
| EMP0003     | Carol Manager | MANAGER | 3333 |

### Customer Self-Service Portal

Customer logs in with their **Account Number** and 4-digit PIN.  
All operations are automatically scoped to their own account — they cannot access other accounts.

---

## Validation Rules

| Field               | Rule                                                  |
| ------------------- | ----------------------------------------------------- |
| Name                | Must not be blank                                     |
| PIN                 | Exactly 4 digits                                      |
| Balance on creation | Savings: >= 500.00 &nbsp; Current: >= 0               |
| Deposit/Withdrawal  | Amount must be > 0                                    |
| Daily withdrawal    | Max 50,000 per account per calendar day               |
| Phone number        | 10–15 digits (Savings accounts only)                  |
| Date of birth       | Must be in the past; customer must be >= 18 years old |
| Registration No.    | Must not be blank (Current accounts only)             |
| Close account       | Balance must be 0 before closing                      |

---

## Account & Employee IDs

- **Account numbers**: 11-digit `long` starting at `10000001000`, auto-incrementing
- **Employee IDs**: formatted as `EMP0001`, `EMP0002`, ... auto-incrementing
- **Transaction IDs**: integer auto-incrementing from 1
- **Audit Entry IDs**: integer auto-incrementing from 1
