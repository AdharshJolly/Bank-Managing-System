package com.talenciaglobal.gdb.repository;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.talenciaglobal.gdb.model.BankEmployee;

public class EmployeeRepository {
    private final Map<String, BankEmployee> store = new HashMap<>();

    public void save(BankEmployee employee) {
        store.put(employee.getEmployeeId(), employee);
    }

    public Optional<BankEmployee> findById(String employeeId) {
        return Optional.ofNullable(store.get(employeeId));
    }

    public Collection<BankEmployee> findAll() {
        return Collections.unmodifiableCollection(store.values());
    }

    public boolean delete(String employeeId) {
        return store.remove(employeeId) != null;
    }
}
