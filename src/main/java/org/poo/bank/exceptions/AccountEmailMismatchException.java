package org.poo.bank.exceptions;

public class AccountEmailMismatchException extends RuntimeException {
    public AccountEmailMismatchException(String email, String iban) {
        super("Account with IBAN " + iban + " doesn't belong to the user with email " + email);
    }
}
