package org.poo.bank.exceptions;

import org.poo.bank.accounts.Account;

public class NotZeroFundsException extends RuntimeException {
    public NotZeroFundsException(Account account) {
        super("Account " + account.getIban() + " does not have balance equal to zero");
    }
}
