package com.talenciaglobal.gdb.service;

import java.util.Collection;

import com.talenciaglobal.gdb.model.Account;
import com.talenciaglobal.gdb.model.Privilege;
import com.talenciaglobal.gdb.model.SavingsAccount;
import com.talenciaglobal.gdb.repository.AccountRepository;

public class AccountService {
    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    // ─── Data Access ───

    public Account findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + id));
    }

    public Collection<Account> findAll() {
        return repository.findAll();
    }

    public Collection<Account> findByName(String name) {
        return repository.findByName(name);
    }

    public void save(Account account) {
        repository.save(account);
    }

    // ─── Business Operations ───

    public void deposit(long accountId, double amount) {
        Account account = findById(accountId);
        account.deposit(amount);
        repository.save(account);
    }

    public void withdraw(long accountId, double amount) {
        Account account = findById(accountId);
        account.withdraw(amount);
        repository.save(account);
    }

    public void transfer(long fromId, long toId, double amount) {
        Account source = findById(fromId);
        Account target = findById(toId);
        source.withdrawTransfer(amount);
        target.depositTransfer(amount);
        repository.save(source);
        repository.save(target);
    }

    public void activateAccount(long accountId) {
        Account account = findById(accountId);
        account.activateAccount();
        repository.save(account);
    }

    public void closeAccount(long accountId) {
        Account account = findById(accountId);
        account.closeAccount();
        repository.save(account);
    }

    public double applyInterest(long accountId) {
        Account account = findById(accountId);
        if (!(account instanceof SavingsAccount sa)) {
            throw new IllegalArgumentException("Interest can only be applied to Savings Accounts.");
        }
        double interest = sa.calculateInterest();
        sa.depositInterest(interest);
        repository.save(sa);
        return interest;
    }

    public void changePrivilege(long accountId, Privilege newPrivilege) {
        Account account = findById(accountId);
        account.setPrivilege(newPrivilege);
        repository.save(account);
    }

    public void unlockCustomerAccount(long accountId) {
        Account account = findById(accountId);
        if (!account.isLoginLocked()) {
            throw new IllegalStateException("Account " + accountId + " is not locked.");
        }
        account.unlockLogin();
        repository.save(account);
    }
}
