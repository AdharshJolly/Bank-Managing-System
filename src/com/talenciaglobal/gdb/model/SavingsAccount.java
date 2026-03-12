package com.talenciaglobal.gdb.model;

import java.time.LocalDate;
import java.time.Period;

public class SavingsAccount extends Account {
    private String gender;
    private LocalDate dateOfBirth;
    private String phoneNumber;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null || !dateOfBirth.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth must be in the past.");
        }
        if (Period.between(dateOfBirth, LocalDate.now()).getYears() < 18) {
            throw new IllegalArgumentException("Customer must be at least 18 years old.");
        }
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("\\d{10,15}")) {
            throw new IllegalArgumentException("Phone number must be 10-15 digits.");
        }
        this.phoneNumber = phoneNumber;
    }

    public double calculateInterest() {
        if (getPrivilege() == null) {
            throw new IllegalStateException("Privilege must be set before calculating interest.");
        }
        return getBalance() * getPrivilege().getInterestRate() / 100;
    }
}
