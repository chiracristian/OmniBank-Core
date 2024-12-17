package org.poo.bank.accounts;

public class AccountFactory {
    private AccountFactory() { }

    public static Account createClassicAccount(String ownerEmail, String currency) {
        return new Account(ownerEmail, currency);
    }

    public static SavingsAccount createSavingsAccount(String ownerEmail, String currency, double interestRate) {
        return new SavingsAccount(ownerEmail, currency, interestRate);
    }
}
