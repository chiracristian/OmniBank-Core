package org.poo.bank.exceptions;

public class NonExistingIbanException extends RuntimeException {
    public NonExistingIbanException(String iban) {
        super("Account with IBAN " + iban + " doesn't exist");
    }
}
