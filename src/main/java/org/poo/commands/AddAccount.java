package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.accounts.Account;
import org.poo.bank.accounts.AccountType;
import org.poo.bank.Bank;
import org.poo.bank.accounts.SavingsAccount;
import org.poo.fileio.CommandInput;

final class AddAccount extends Command {
    public static final String COMMAND = "addAccount";

    private final String email;
    private final String currency;
    private final AccountType accountType;
    private final double interestRate;

    AddAccount(final CommandInput input) {
        super(input);
        this.email = input.getEmail();
        this.currency = input.getCurrency();
        this.accountType = AccountType.fromString(input.getAccountType());
        this.interestRate = input.getInterestRate();
    }

    public ObjectNode executeAndGetOutput(final Bank bank, final ObjectMapper mapper) {
        Account addedAccount;
        switch (accountType) {
            case CLASSIC -> addedAccount = new Account(email, currency);
            case SAVINGS -> addedAccount = new SavingsAccount(email, currency, interestRate);
            default -> throw new IllegalArgumentException("There are no accounts of type "
                    + accountType.getString());
        }
        bank.addAccount(addedAccount, email, timestamp);
        return null;
    }
}
