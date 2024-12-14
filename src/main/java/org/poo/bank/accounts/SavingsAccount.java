package org.poo.bank.accounts;

import lombok.Getter;

@Getter
public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String currency, double interestRate) {
        super(currency);
        this.interestRate = interestRate;
    }
}
