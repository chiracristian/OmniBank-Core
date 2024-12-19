package org.poo.exceptions;

public class NonExistingIbanException extends RuntimeException {
    public NonExistingIbanException(final String iban) {
        super("Account with IBAN " + iban + " doesn't exist");
    }
}
