package com.talenciaglobal.gdb.model;

import java.time.LocalDateTime;

public class Transaction {
    private static int transactionIdSeed = 1;
    private final int transactionId;
    private final TransactionType type;
    private final double amount;
    private final double balanceAfter;
    private final LocalDateTime timestamp;
    private final long accountNumber;

    public Transaction(TransactionType type, double amount, double balanceAfter, long accountNumber) {
        this.transactionId = transactionIdSeed++;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
        this.accountNumber = accountNumber;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public long getAccountNumber() {
        return accountNumber;
    }
}
