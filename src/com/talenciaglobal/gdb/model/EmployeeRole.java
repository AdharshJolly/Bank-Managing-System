package com.talenciaglobal.gdb.model;

public enum EmployeeRole {
    TELLER, MANAGER, ADMIN;

    public boolean canCreateAccounts() {
        return this == MANAGER || this == ADMIN;
    }

    public boolean canActivateAccounts() {
        return this == MANAGER || this == ADMIN;
    }

    public boolean canCloseAccounts() {
        return this == MANAGER || this == ADMIN;
    }

    public boolean canApplyInterest() {
        return this == MANAGER || this == ADMIN;
    }

    public boolean canListAllAccounts() {
        return this == MANAGER || this == ADMIN;
    }

    public boolean canUnlockCustomers() {
        return this == MANAGER || this == ADMIN;
    }

    public boolean canUnlockEmployees() {
        return this == ADMIN;
    }

    public boolean canChangePrivilege() {
        return this == MANAGER || this == ADMIN;
    }

    public boolean canManageEmployees() {
        return this == ADMIN;
    }
}
