package com.talenciaglobal.gdb.controller;

import java.time.LocalDate;
import java.util.Scanner;

import com.talenciaglobal.gdb.model.Account;
import com.talenciaglobal.gdb.model.CurrentAccount;
import com.talenciaglobal.gdb.model.Privilege;
import com.talenciaglobal.gdb.model.SavingsAccount;
import com.talenciaglobal.gdb.model.Transaction;
import com.talenciaglobal.gdb.repository.AccountRepository;

public class AccountController {
    private final Scanner scanner;
    private final AccountRepository repository;

    public AccountController(Scanner scanner, AccountRepository repository) {
        this.scanner = scanner;
        this.repository = repository;
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
        repository.save(account);
        System.out.println("Account created successfully. Account Number: " + account.getAccountNumber());
        return account;
    }

    public void activateAccount() {
        Account account = findAccountOrThrow();
        account.activateAccount();
        repository.save(account);
        System.out.println("Account " + account.getAccountNumber() + " activated successfully.");
    }

    public void closeAccount() {
        Account account = findAccountOrThrow();
        account.closeAccount();
        repository.save(account);
        System.out.println("Account " + account.getAccountNumber() + " closed.");
    }

    public void deposit() {
        Account account = findAccountOrThrow();
        System.out.print("Deposit amount: ");
        double amount = Double.parseDouble(scanner.nextLine().trim());
        account.deposit(amount);
        repository.save(account);
        System.out.printf("Deposited %.2f. New balance: %.2f%n", amount, account.getBalance());
    }

    public void withdraw() {
        Account account = findAccountOrThrow();
        System.out.print("Withdrawal amount: ");
        double amount = Double.parseDouble(scanner.nextLine().trim());
        account.withdraw(amount);
        repository.save(account);
        System.out.printf("Withdrew %.2f. New balance: %.2f%n", amount, account.getBalance());
    }

    public void transfer() {
        System.out.print("Source Account Number: ");
        long fromId = Long.parseLong(scanner.nextLine().trim());
        System.out.print("Target Account Number: ");
        long toId = Long.parseLong(scanner.nextLine().trim());
        System.out.print("Transfer amount: ");
        double amount = Double.parseDouble(scanner.nextLine().trim());

        Account source = repository.findById(fromId)
                .orElseThrow(() -> new IllegalArgumentException("Source account not found: " + fromId));
        Account target = repository.findById(toId)
                .orElseThrow(() -> new IllegalArgumentException("Target account not found: " + toId));

        source.withdrawTransfer(amount);
        target.depositTransfer(amount);
        repository.save(source);
        repository.save(target);
        System.out.printf("Transferred %.2f from %d to %d.%n", amount, fromId, toId);
    }

    public void listAll() {
        var accounts = repository.findAll();
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }
        System.out.println("\n========= All Accounts =========");
        for (Account a : accounts) {
            System.out.printf("%-15d %-25s %-10s %10.2f%n",
                    a.getAccountNumber(), a.getName(),
                    a.isActive() ? "ACTIVE" : "INACTIVE", a.getBalance());
        }
        System.out.println("=================================");
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
            System.out.printf("Interest Earned: %.2f (on current balance)%n", sa.calculateInterest());
        } else if (account instanceof CurrentAccount ca) {
            System.out.println("--- Business Info ---");
            System.out.println("Company        : " + ca.getCompanyName());
            System.out.println("Website        : " + ca.getWebsite());
            System.out.println("Reg. Number    : " + ca.getRegistrationNumber());
            System.out.printf("Overdraft Limit: %.0f%n", ca.getPrivilege().getOverdraftLimit());
        }
        System.out.println("---------------------------------------");
    }

    public void applyInterest() {
        Account account = findAccountOrThrow();
        if (!(account instanceof SavingsAccount sa)) {
            throw new IllegalArgumentException("Interest can only be applied to Savings Accounts.");
        }
        double interest = sa.calculateInterest();
        sa.depositInterest(interest);
        repository.save(sa);
        System.out.printf("Interest of %.2f (%.1f%%) applied. New balance: %.2f%n",
                interest, sa.getPrivilege().getInterestRate(), sa.getBalance());
    }

    public void viewTransactionHistory() {
        Account account = findAccountOrThrow();
        var txns = account.getTransactions();
        if (txns.isEmpty()) {
            System.out.println("No transactions found for account " + account.getAccountNumber() + ".");
            return;
        }
        System.out.println("\n===== Transaction History: " + account.getAccountNumber() + " =====");
        System.out.printf("%-6s %-18s %12s %14s  %s%n", "TxID", "Type", "Amount", "Balance After", "Timestamp");
        System.out.println("-".repeat(75));
        for (Transaction t : txns) {
            System.out.printf("%-6d %-18s %12.2f %14.2f  %s%n",
                    t.getTransactionId(),
                    t.getType(),
                    t.getAmount(),
                    t.getBalanceAfter(),
                    t.getTimestamp().toString().replace("T", " ").substring(0, 19));
        }
        System.out.println("=".repeat(75));
    }

    private Account findAccountOrThrow() {
        System.out.print("Account Number: ");
        long id = Long.parseLong(scanner.nextLine().trim());
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + id));
    }
}
