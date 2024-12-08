package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.AccountType;
import org.poo.bank.Bank;
import org.poo.bank.Currency;

class AddAccount extends Command {
    private static final String COMMAND = "addAccount";
    private final String email;
    private final Currency currency;
    private final AccountType accountType;
    private final double interestRate;

    AddAccount(String email, String currency, String accountType, int timestamp, double interestRate) {
        this.email = email;
        this.currency = Currency.fromString(currency);
        this.accountType = AccountType.fromString(accountType);
        this.timestamp = timestamp;
        this.interestRate = interestRate;
    }

    public ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper) {
        switch (accountType) {
            case AccountType.CLASSIC:
                bank.addAccount(email, currency, accountType);
                break;
            case AccountType.SAVINGS:
                bank.addAccount(email, currency, accountType, interestRate);
                break;
        }
        return null;
    }
}
