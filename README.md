# GlobalDigitalBank

A Java console-based digital banking application with a layered MVC-like architecture.

---

## Features

- Create **Savings** and **Current** accounts with full validation
- **Privilege tiers** (Platinum / Gold / Silver / Bronze) controlling interest rates and overdraft limits
- Deposit, withdraw, and transfer between accounts
- Account lifecycle: activate and close accounts
- Interest calculation and application for Savings accounts
- Overdraft support for Current accounts based on privilege tier
- Full **transaction history** per account (deposit, withdrawal, transfer, interest)

---

## Project Structure

```
src/
├── App.java                                  ← Entry point / interactive menu
└── com/talenciaglobal/gdb/
    ├── model/
    │   ├── User.java                         ← Interface: customer identity + PIN auth
    │   ├── Employee.java                     ← Interface: bank staff identity + role
    │   ├── EmployeeRole.java                 ← Enum: TELLER, MANAGER, ADMIN
    │   ├── BankEmployee.java                 ← Implements Employee (EMP0001 IDs)
    │   ├── Account.java                      ← Implements User; base class (deposit, withdraw, lifecycle)
    │   ├── SavingsAccount.java               ← Personal accounts (interest, DOB, phone)
    │   ├── CurrentAccount.java               ← Business accounts (overdraft logic)
    │   ├── Privilege.java                    ← Tier enum with interest rate + overdraft limit
    │   ├── Transaction.java                  ← Transaction record (type, amount, timestamp)
    │   └── TransactionType.java              ← Enum: DEPOSIT, WITHDRAWAL, TRANSFER_DEBIT/CREDIT, INTEREST
    ├── repository/
    │   └── AccountRepository.java            ← In-memory HashMap store (CRUD)
    └── controller/
        └── AccountController.java            ← Orchestrates all operations, formats output
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

## Getting Started

### Prerequisites

- Java 17 or higher
- VS Code with the [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)

### Run

**Using VS Code:**
Open `src/App.java` and click **Run**.

**Using terminal:**

```bash
# From project root
javac -d bin src/com/talenciaglobal/gdb/model/TransactionType.java \
             src/com/talenciaglobal/gdb/model/Transaction.java \
             src/com/talenciaglobal/gdb/model/Privilege.java \
             src/com/talenciaglobal/gdb/model/Account.java \
             src/com/talenciaglobal/gdb/model/SavingsAccount.java \
             src/com/talenciaglobal/gdb/model/CurrentAccount.java \
             src/com/talenciaglobal/gdb/repository/AccountRepository.java \
             src/com/talenciaglobal/gdb/controller/AccountController.java \
             src/App.java

java -cp bin App
```

---

## Menu Options

```
 1. Create Account
 2. Activate Account
 3. Deposit
 4. Withdraw
 5. Transfer
 6. View Account
 7. List All Accounts
 8. Close Account
 9. Apply Interest  (Savings accounts only)
10. View Transaction History
 0. Exit
```

---

## Account Numbers

Account numbers are 11-digit numeric identifiers starting at `10000001000` and auto-incrementing.

---

## Validation Rules

| Field            | Rule                                          |
| ---------------- | --------------------------------------------- |
| Name             | Must not be blank                             |
| PIN              | Exactly 4 digits                              |
| Balance          | Must be >= 0 on creation; amounts must be > 0 |
| Phone number     | 10–15 digits (Savings accounts)               |
| Date of birth    | Must be in the past; customer >= 18 years old |
| Registration No. | Must not be blank (Current accounts)          |
