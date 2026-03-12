package com.talenciaglobal.gdb.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Account implements User {
    private static long accountNumberSeed = 10000001000L;
    private final long accountNumber;
    private String name;
    private String pinNumber;
    private double balance;
    private boolean isActive;
    private LocalDate activatedDate;
    private LocalDate closedDate;
    // Has-A: one Account has one Privilege
    private Privilege privilege;
    private final List<Transaction> transactions = new ArrayList<>();

    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private int failedLoginAttempts = 0;
    private boolean loginLocked = false;

    private static final double DAILY_WITHDRAWAL_LIMIT = 50_000.0;
    private LocalDate lastWithdrawalDate = null;
    private double dailyWithdrawnAmount = 0.0;

    public Account() {
        this.accountNumber = accountNumberSeed++;
    }

    public static long getAccountNumberSeed() {
        return accountNumberSeed;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    @Override
    public boolean authenticate(String pin) {
        return this.pinNumber != null && this.pinNumber.equals(pin);
    }

    public boolean isLoginLocked() {
        return loginLocked;
    }

    public int getRemainingLoginAttempts() {
        return MAX_LOGIN_ATTEMPTS - failedLoginAttempts;
    }

    public void recordFailedLoginAttempt() {
        if (loginLocked)
            return;
        failedLoginAttempts++;
        if (failedLoginAttempts >= MAX_LOGIN_ATTEMPTS) {
            loginLocked = true;
        }
    }

    public void resetLoginAttempts() {
        failedLoginAttempts = 0;
        loginLocked = false;
    }

    public void unlockLogin() {
        loginLocked = false;
        failedLoginAttempts = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank.");
        }
        this.name = name;
    }

    public String getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(String pinNumber) {
        if (pinNumber == null || !pinNumber.matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN must be exactly 4 digits.");
        }
        this.pinNumber = pinNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Initial balance must be >= 0.");
        }
        this.balance = balance;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDate getActivatedDate() {
        return activatedDate;
    }

    public LocalDate getClosedDate() {
        return closedDate;
    }

    public Privilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Privilege privilege) {
        if (privilege == null) {
            throw new IllegalArgumentException("Privilege must not be null.");
        }
        this.privilege = privilege;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public void activateAccount() {
        if (isActive) {
            throw new IllegalStateException("Account is already active.");
        }
        this.isActive = true;
        this.activatedDate = LocalDate.now();
    }

    public void closeAccount() {
        if (!isActive) {
            throw new IllegalStateException("Account is not active.");
        }
        if (balance != 0) {
            throw new IllegalStateException(
                    String.format(
                            "Cannot close account with a non-zero balance of %.2f. Please withdraw or transfer all funds first.",
                            balance));
        }
        this.isActive = false;
        this.closedDate = LocalDate.now();
    }

    public void deposit(double amount) {
        if (!isActive) {
            throw new IllegalStateException("Cannot deposit into an inactive account.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be > 0.");
        }
        this.balance += amount;
        transactions.add(new Transaction(TransactionType.DEPOSIT, amount, this.balance, this.accountNumber));
    }

    public void withdraw(double amount) {
        if (!isActive) {
            throw new IllegalStateException("Cannot withdraw from an inactive account.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be > 0.");
        }
        double minimumBalance = getMinimumBalance();
        if (balance - amount < minimumBalance) {
            throw new IllegalArgumentException(
                    "Insufficient funds. Max withdrawable: " + (balance - minimumBalance));
        }
        checkAndUpdateDailyLimit(amount);
        this.balance -= amount;
        transactions.add(new Transaction(TransactionType.WITHDRAWAL, amount, this.balance, this.accountNumber));
    }

    // Called internally for transfer credits — records TRANSFER_CREDIT type
    public void depositTransfer(double amount) {
        if (!isActive) {
            throw new IllegalStateException("Cannot deposit into an inactive account.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be > 0.");
        }
        this.balance += amount;
        transactions.add(new Transaction(TransactionType.TRANSFER_CREDIT, amount, this.balance, this.accountNumber));
    }

    // Called internally for transfer debits — records TRANSFER_DEBIT type
    public void withdrawTransfer(double amount) {
        if (!isActive) {
            throw new IllegalStateException("Cannot withdraw from an inactive account.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be > 0.");
        }
        double minimumBalance = getMinimumBalance();
        if (balance - amount < minimumBalance) {
            throw new IllegalArgumentException(
                    "Insufficient funds. Max withdrawable: " + (balance - minimumBalance));
        }
        checkAndUpdateDailyLimit(amount);
        this.balance -= amount;
        transactions.add(new Transaction(TransactionType.TRANSFER_DEBIT, amount, this.balance, this.accountNumber));
    }

    // Used by applyInterest — records INTEREST type
    public void depositInterest(double amount) {
        if (!isActive) {
            throw new IllegalStateException("Cannot apply interest to an inactive account.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Interest amount must be > 0.");
        }
        this.balance += amount;
        transactions.add(new Transaction(TransactionType.INTEREST, amount, this.balance, this.accountNumber));
    }

    // Subclasses override this to define the floor balance (e.g. overdraft for
    // CurrentAccount).
    protected double getMinimumBalance() {
        return 0;
    }

    private void checkAndUpdateDailyLimit(double amount) {
        LocalDate today = LocalDate.now();
        if (lastWithdrawalDate == null || !lastWithdrawalDate.equals(today)) {
            dailyWithdrawnAmount = 0.0;
            lastWithdrawalDate = today;
        }
        if (dailyWithdrawnAmount + amount > DAILY_WITHDRAWAL_LIMIT) {
            throw new IllegalArgumentException(String.format(
                    "Daily withdrawal limit of %.2f reached. Already withdrawn: %.2f today.",
                    DAILY_WITHDRAWAL_LIMIT, dailyWithdrawnAmount));
        }
        dailyWithdrawnAmount += amount;
    }
}
