package org.poo.bank;

import lombok.Getter;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;

import java.util.HashMap;

@Getter
public class Bank {
    /**
     * Users, referenced by their email
     */
    private final HashMap<String, User> users;

    /**
     * Accounts, referenced by their IBAN
     */
    private final HashMap<String, Account> accounts;

    public Bank(ObjectInput input) {
        users = new HashMap<>();

        for (UserInput userIn : input.getUsers()) {
            users.put(userIn.getEmail(), new User(userIn));
        }

        accounts = new HashMap<>();
    }

    public void addAccount(String email, Currency currency, AccountType type) {
        addAccount(email, currency, type ,0.0);
    }

    public void addAccount(String email, Currency currency, AccountType type, double interestRate) {
        Account addedAccount = users.get(email).addAccount(currency, type, interestRate);
        accounts.put(addedAccount.getIban(), addedAccount);
    }
}
