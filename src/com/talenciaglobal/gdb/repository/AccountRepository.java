package com.talenciaglobal.gdb.repository;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.talenciaglobal.gdb.model.Account;

public class AccountRepository {
    private final Map<Long, Account> store = new HashMap<>();

    public void save(Account account) {
        store.put(account.getAccountNumber(), account);
    }

    public Optional<Account> findById(long accountNumber) {
        return Optional.ofNullable(store.get(accountNumber));
    }

    public Collection<Account> findAll() {
        return Collections.unmodifiableCollection(store.values());
    }

    public boolean delete(long accountNumber) {
        return store.remove(accountNumber) != null;
    }

    public boolean existsById(long accountNumber) {
        return store.containsKey(accountNumber);
    }
}
