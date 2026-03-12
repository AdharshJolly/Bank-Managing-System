package com.talenciaglobal.gdb.model;

/**
 * Represents a bank customer who owns an account and authenticates via PIN.
 */
public interface User {
    long getAccountNumber();

    String getName();

    boolean authenticate(String pin);
}
