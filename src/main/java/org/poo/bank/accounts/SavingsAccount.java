package org.poo.bank.accounts;

import lombok.Getter;

@Getter
public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String ownerEmail, String currency, double interestRate) {
        super(ownerEmail, currency);
        this.interestRate = interestRate;
    }

    @Override
    public String getType() {
        return AccountType.SAVINGS.getString();
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
        // TODO: add transaction for interest rate
    }
}
