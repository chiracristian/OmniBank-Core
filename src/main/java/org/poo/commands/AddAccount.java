package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.accounts.Account;
import org.poo.bank.accounts.AccountFactory;
import org.poo.bank.accounts.AccountType;
import org.poo.bank.Bank;

class AddAccount extends Command {
    public static final String COMMAND = "addAccount";

    private final String email;
    private final String currency;
    private final AccountType accountType;
    private final double interestRate;

    AddAccount(String email, String currency, String accountType, int timestamp, double interestRate) {
        this.email = email;
        this.currency = currency;
        this.accountType = AccountType.fromString(accountType);
        this.timestamp = timestamp;
        this.interestRate = interestRate;
    }

    public ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper) {
        Account addedAccount;
        switch (accountType) {
            case CLASSIC -> addedAccount = AccountFactory.createClassicAccount(currency);
            case SAVINGS -> addedAccount = AccountFactory.createSavingsAccount(currency, interestRate);
            default -> throw new IllegalArgumentException("There are no accounts of type " + accountType.getString());
        }
        bank.addAccount(addedAccount, email);
        return null;
    }
}
