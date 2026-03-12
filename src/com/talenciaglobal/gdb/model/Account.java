package com.talenciaglobal.gdb.model;

import java.time.LocalDate;

public class Account {
    private static int accountNumberSeed = 1000;
    private final int accountNumber;
    private String name;
    private String pinNumber;
    private double balance;
    private boolean isActive;
    private LocalDate activatedDate;
    private LocalDate closedDate;
    // Has-A: one Account has one Privilege
    private Privilege privilege;

    public Account() {
        this.accountNumber = accountNumberSeed++;
    }

    public static int getAccountNumberSeed() {
        return accountNumberSeed;
    }

    public int getAccountNumber() {
        return accountNumber;
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
        this.balance -= amount;
    }

    // Subclasses override this to define the floor balance (e.g. overdraft for
    // CurrentAccount).
    protected double getMinimumBalance() {
        return 0;
    }
}
