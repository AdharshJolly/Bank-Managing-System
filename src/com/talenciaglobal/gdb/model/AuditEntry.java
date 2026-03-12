package com.talenciaglobal.gdb.model;

import java.time.LocalDateTime;

public class AuditEntry {
    private static int entrySeed = 1;
    private final int entryId;
    private final String employeeId;
    private final String employeeName;
    private final String action;
    private final Long targetAccountNumber; // nullable
    private final LocalDateTime timestamp;

    public AuditEntry(String employeeId, String employeeName, String action, Long targetAccountNumber) {
        this.entryId = entrySeed++;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.action = action;
        this.targetAccountNumber = targetAccountNumber;
        this.timestamp = LocalDateTime.now();
    }

    public int getEntryId() {
        return entryId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getAction() {
        return action;
    }

    public Long getTargetAccountNumber() {
        return targetAccountNumber;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
