package org.poo.bank.exceptions;

import org.poo.bank.accounts.Account;

public class NotSavingsAccountException extends RuntimeException {
    public NotSavingsAccountException(Account account) {
        super("Account " + account.getIban() + " is not a savings account");
    }
}
