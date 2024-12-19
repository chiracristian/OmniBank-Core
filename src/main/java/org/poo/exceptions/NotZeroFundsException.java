package org.poo.exceptions;

import org.poo.bank.accounts.Account;

public class NotZeroFundsException extends RuntimeException {
    public NotZeroFundsException(final Account account) {
        super("Account " + account.getIban() + " does not have balance equal to zero");
    }
}
