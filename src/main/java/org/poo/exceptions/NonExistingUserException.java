package org.poo.exceptions;

public class NonExistingUserException extends RuntimeException {
    public NonExistingUserException(final String email) {
        super("User with email " + email + " doesn't exist");
    }
}
