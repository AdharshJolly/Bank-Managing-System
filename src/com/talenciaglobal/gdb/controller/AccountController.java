package com.talenciaglobal.gdb.controller;

import java.time.LocalDate;
import java.util.Scanner;

import com.talenciaglobal.gdb.model.Account;
import com.talenciaglobal.gdb.model.CurrentAccount;
import com.talenciaglobal.gdb.model.Privilege;
import com.talenciaglobal.gdb.model.SavingsAccount;

public class AccountController {
    private final Scanner scanner;

    public AccountController(Scanner scanner) {
        this.scanner = scanner;
    }

    public Account create() {
        System.out.println("\n-------- Create New Account --------");
        System.out.println("Account Type:");
        System.out.println("  1. Savings Account");
        System.out.println("  2. Current Account");
        System.out.print("Choose (1/2): ");
        int type = Integer.parseInt(scanner.nextLine().trim());
        if (type != 1 && type != 2) {
            throw new IllegalArgumentException("Invalid account type. Choose 1 or 2.");
        }

        System.out.print("Full Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("PIN (4 digits): ");
        String pin = scanner.nextLine().trim();

        System.out.print("Initial Balance: ");
        double balance = Double.parseDouble(scanner.nextLine().trim());

        System.out.println("Privilege Tier: 1=Platinum  2=Gold  3=Silver  4=Bronze");
        System.out.print("Choose (1-4): ");
        int privChoice = Integer.parseInt(scanner.nextLine().trim());
        if (privChoice < 1 || privChoice > 4) {
            throw new IllegalArgumentException("Invalid privilege choice.");
        }
        Privilege privilege = Privilege.values()[privChoice - 1];

        Account account;
        if (type == 1) {
            SavingsAccount sa = new SavingsAccount();
            System.out.print("Gender (M/F/Other): ");
            sa.setGender(scanner.nextLine().trim());
            System.out.print("Date of Birth (YYYY-MM-DD): ");
            sa.setDateOfBirth(LocalDate.parse(scanner.nextLine().trim()));
            System.out.print("Phone Number (10-15 digits): ");
            sa.setPhoneNumber(scanner.nextLine().trim());
            account = sa;
        } else {
            CurrentAccount ca = new CurrentAccount();
            System.out.print("Company Name: ");
            ca.setCompanyName(scanner.nextLine().trim());
            System.out.print("Website: ");
            ca.setWebsite(scanner.nextLine().trim());
            System.out.print("Registration Number: ");
            ca.setRegistrationNumber(scanner.nextLine().trim());
            account = ca;
        }

        account.setName(name);
        account.setPinNumber(pin);
        account.setBalance(balance);
        account.setPrivilege(privilege);
        return account;
    }

    public void display(Account account) {
        System.out.println("\n------------Account Details------------");
        System.out.println("Account Number : " + account.getAccountNumber());
        System.out.println("Name           : " + account.getName());
        System.out.printf("Balance        : %.2f%n", account.getBalance());
        System.out.println("Privilege      : " + account.getPrivilege());
        System.out.println("Status         : " + (account.isActive() ? "ACTIVE" : "INACTIVE"));
        System.out.println("Activated      : " + account.getActivatedDate());
        System.out.println("Closed         : " + account.getClosedDate());
        if (account instanceof SavingsAccount sa) {
            System.out.println("--- Savings Info ---");
            System.out.println("Gender         : " + sa.getGender());
            System.out.println("Date of Birth  : " + sa.getDateOfBirth());
            System.out.println("Phone          : " + sa.getPhoneNumber());
            System.out.printf("Interest Rate  : %.1f%%%n", sa.getPrivilege().getInterestRate());
        } else if (account instanceof CurrentAccount ca) {
            System.out.println("--- Business Info ---");
            System.out.println("Company        : " + ca.getCompanyName());
            System.out.println("Website        : " + ca.getWebsite());
            System.out.println("Reg. Number    : " + ca.getRegistrationNumber());
            System.out.printf("Overdraft Limit: %.0f%n", ca.getPrivilege().getOverdraftLimit());
        }
        System.out.println("---------------------------------------");
    }
}
