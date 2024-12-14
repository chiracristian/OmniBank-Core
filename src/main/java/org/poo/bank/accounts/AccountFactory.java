package org.poo.bank.accounts;

public class AccountFactory {
    private AccountFactory() { }

    public static Account createClassicAccount(String currency) {
        return new Account(currency);
    }

    public static SavingsAccount createSavingsAccount(String currency, double interestRate) {
        return new SavingsAccount(currency, interestRate);
    }
}
