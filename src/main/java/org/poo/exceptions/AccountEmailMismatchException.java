package org.poo.exceptions;

public class AccountEmailMismatchException extends RuntimeException {
    public AccountEmailMismatchException(final String email, final String iban) {
        super("Account with IBAN " + iban + " doesn't belong to the user with email " + email);
    }
}
