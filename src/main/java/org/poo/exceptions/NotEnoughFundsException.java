package org.poo.exceptions;

import org.poo.bank.accounts.Account;

public class NotEnoughFundsException extends RuntimeException {
    public NotEnoughFundsException(final Account account) {
        super("Account " + account.getIban() + " does not have enough funds");
    }
}
