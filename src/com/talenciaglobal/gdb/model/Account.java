package com.talenciaglobal.gdb.model;

import java.time.LocalDate;

public class Account {
    private static int accountNumberSeed = 1000;
    private int accountNumber;
    private String name;
    private String pinNumber;
    private double balance;
    private boolean isActive;
    private LocalDate activatedDate;
    private LocalDate closedDate;
    // Has-A relationship of Account with Privilege. One Account has one Privilege.
    private Privilege privilege;

    // public Account(String name, String pinNumber, double balance) {
    // this.accountNumber = accountNumberSeed++;
    // this.name = name;
    // this.pinNumber = pinNumber;
    // this.balance = balance;
    // }

    public static int getAccountNumberSeed() {
        return accountNumberSeed;
    }

    public static void setAccountNumberSeed(int accountNumberSeed) {
        Account.accountNumberSeed = accountNumberSeed;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(String pinNumber) {
        this.pinNumber = pinNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDate getActivatedDate() {
        return activatedDate;
    }

    public void setActivatedDate(LocalDate activatedDate) {
        this.activatedDate = activatedDate;
    }

    public LocalDate getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(LocalDate closedDate) {
        this.closedDate = closedDate;
    }

    public Privilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }
}
