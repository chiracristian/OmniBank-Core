package org.poo.bank.exceptions;

public class NonExistingUserException extends RuntimeException {
    public NonExistingUserException(String email) {
        super("User with email " + email + " doesn't exist");
    }
}
