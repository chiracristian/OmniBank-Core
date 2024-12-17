package org.poo.bank.exceptions;

public class NonExistingCardException extends RuntimeException {
    public NonExistingCardException(String cardNumber) {
        super("Card with " + cardNumber + " doesn't exist");
    }
}
