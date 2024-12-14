package org.poo.bank;

public class NonExistingCardException extends RuntimeException {
    public NonExistingCardException(String cardNumber) {
        super("Card with " + cardNumber + " doesn't exist");
    }
}
