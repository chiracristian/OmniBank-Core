package org.poo.bank;

import lombok.Getter;

@Getter
public class SavingsAccount extends Account {
    private double interestRate;
    SavingsAccount(Currency currency, double interestRate) {
        super(currency);
        this.interestRate = interestRate;
    }
}
