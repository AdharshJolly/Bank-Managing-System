package com.talenciaglobal.gdb.model;

public class BankEmployee implements Employee {
    private static int employeeIdSeed = 1;
    private final String employeeId;
    private String employeeName;
    private EmployeeRole role;

    public BankEmployee(String employeeName, EmployeeRole role) {
        if (employeeName == null || employeeName.isBlank()) {
            throw new IllegalArgumentException("Employee name must not be blank.");
        }
        if (role == null) {
            throw new IllegalArgumentException("Employee role must not be null.");
        }
        this.employeeId = String.format("EMP%04d", employeeIdSeed++);
        this.employeeName = employeeName;
        this.role = role;
    }

    @Override
    public String getEmployeeId() {
        return employeeId;
    }

    @Override
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        if (employeeName == null || employeeName.isBlank()) {
            throw new IllegalArgumentException("Employee name must not be blank.");
        }
        this.employeeName = employeeName;
    }

    @Override
    public EmployeeRole getRole() {
        return role;
    }

    public void setRole(EmployeeRole role) {
        if (role == null) {
            throw new IllegalArgumentException("Employee role must not be null.");
        }
        this.role = role;
    }

    @Override
    public String toString() {
        return employeeId + " | " + employeeName + " | " + role;
    }
}
