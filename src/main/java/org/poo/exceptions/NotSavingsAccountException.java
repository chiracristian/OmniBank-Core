package org.poo.exceptions;

import org.poo.bank.accounts.Account;

public class NotSavingsAccountException extends RuntimeException {
    public NotSavingsAccountException(final Account account) {
        super("Account " + account.getIban() + " is not a savings account");
    }
}
