package com.talenciaglobal.gdb.model;

public enum Privilege {
    Platinum(5.0, 50_000),
    Gold(4.0, 30_000),
    Silver(3.0, 10_000),
    Bronze(2.0, 0);

    private final double interestRate;
    private final double overdraftLimit;

    Privilege(double interestRate, double overdraftLimit) {
        this.interestRate = interestRate;
        this.overdraftLimit = overdraftLimit;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }
}
