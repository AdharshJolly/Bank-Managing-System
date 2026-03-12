package com.talenciaglobal.gdb.controller;

import com.talenciaglobal.gdb.model.Account;

public class AccountController {
    public Account create() {
        return null;
    }

    public void display(Account account) {
        System.out.println("------------Account Details------------");
        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Name: " + account.getName());
        System.out.println("Balance: " + account.getBalance());
        System.out.println("Privilege: " + account.getPrivilege());
        System.out.println("---------------------------------------");
    }
}
