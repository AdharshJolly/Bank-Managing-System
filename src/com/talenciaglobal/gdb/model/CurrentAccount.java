package com.talenciaglobal.gdb.model;

public class CurrentAccount extends Account {
    private String companyName;
    private String website;
    private String registrationNumber;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        if (registrationNumber == null || registrationNumber.isBlank()) {
            throw new IllegalArgumentException("Registration number must not be blank.");
        }
        this.registrationNumber = registrationNumber;
    }

    // Allows balance to go negative up to the overdraft limit for this privilege
    // tier.
    @Override
    protected double getMinimumBalance() {
        return getPrivilege() != null ? -getPrivilege().getOverdraftLimit() : 0;
    }
}
