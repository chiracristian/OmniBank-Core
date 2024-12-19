package org.poo.bank.exceptions;

public class NonExistingCardException extends RuntimeException {
    public NonExistingCardException(String cardNumber) {
        super("Card with number " + cardNumber + " doesn't exist");
    }
}
