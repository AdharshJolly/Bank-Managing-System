package com.talenciaglobal.gdb.model;

/**
 * Represents a bank employee who can perform operations on accounts.
 */
public interface Employee {
    String getEmployeeId();

    String getEmployeeName();

    EmployeeRole getRole();
}
