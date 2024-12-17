package org.poo.bank.exceptions;

import org.poo.bank.accounts.Account;

public class NotEnoughFundsException extends RuntimeException {
    public NotEnoughFundsException(Account account) {
        super("Account " + account.getIban() + " does not have enough funds");
    }
}
