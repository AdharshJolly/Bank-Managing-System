package com.talenciaglobal.gdb.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.talenciaglobal.gdb.model.AuditEntry;

public class AuditLogRepository {
    private final List<AuditEntry> log = new ArrayList<>();

    public void log(AuditEntry entry) {
        log.add(entry);
    }

    public List<AuditEntry> findAll() {
        return Collections.unmodifiableList(log);
    }

    public List<AuditEntry> findByEmployee(String employeeId) {
        return log.stream()
                .filter(e -> e.getEmployeeId().equals(employeeId))
                .collect(Collectors.toList());
    }
}
