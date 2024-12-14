package org.poo.bank.accounts;

public class NotEnoughFundsException extends RuntimeException {
    public NotEnoughFundsException(Account account) {
        super("Account " + account.getIban() + " does not have enough funds");
    }
}
