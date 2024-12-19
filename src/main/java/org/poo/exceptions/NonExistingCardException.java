package org.poo.exceptions;

public class NonExistingCardException extends RuntimeException {
    public NonExistingCardException(final String cardNumber) {
        super("Card with number " + cardNumber + " doesn't exist");
    }
}
