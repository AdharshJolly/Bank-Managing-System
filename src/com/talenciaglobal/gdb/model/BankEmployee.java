package com.talenciaglobal.gdb.model;

public class BankEmployee implements Employee {
    private static int employeeIdSeed = 1;
    private final String employeeId;
    private String employeeName;
    private EmployeeRole role;
    private String pin;

    public BankEmployee(String employeeName, EmployeeRole role, String pin) {
        if (employeeName == null || employeeName.isBlank()) {
            throw new IllegalArgumentException("Employee name must not be blank.");
        }
        if (role == null) {
            throw new IllegalArgumentException("Employee role must not be null.");
        }
        if (pin == null || !pin.matches("\\d{4}")) {
            throw new IllegalArgumentException("Employee PIN must be exactly 4 digits.");
        }
        this.employeeId = String.format("EMP%04d", employeeIdSeed++);
        this.employeeName = employeeName;
        this.role = role;
        this.pin = pin;
    }

    @Override
    public boolean authenticate(String pin) {
        return this.pin != null && this.pin.equals(pin);
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
