package org.poo.bank;

import lombok.Getter;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;

import java.util.HashMap;

@Getter
public class Bank {
    /**
     * Users, referenced by their email
     */
    private final HashMap<String, User> users;

    /**
     * Accounts, referenced by their IBAN
     */
    private final HashMap<String, Account> accounts;

    /**
     * Cards, referenced by their number
     */
    private final HashMap<String, Card> cards;

    public Bank(ObjectInput input) {
        users = new HashMap<>();

        for (UserInput userIn : input.getUsers()) {
            users.put(userIn.getEmail(), new User(userIn));
        }

        accounts = new HashMap<>();
        cards = new HashMap<>();
    }

    private Account getAccountRef(String account, String email) {
        Account refAccount = accounts.get(account);
        if (refAccount == null) {
            throw new IllegalArgumentException("Account " + account + " doesn't exist!");
        }
        if (!users.get(email).getAccounts().contains(refAccount)) {
            throw new IllegalArgumentException("Email of account " + account + " is not " + email);
        }

        return refAccount;
    }

    public void addAccount(String email, Currency currency, AccountType type) {
        addAccount(email, currency, type ,0.0);
    }

    public void addAccount(String email, Currency currency, AccountType type, double interestRate) {
        Account addedAccount = users.get(email).addAccount(currency, type, interestRate);
        accounts.put(addedAccount.getIban(), addedAccount);
    }

    public void deleteAccount(String account, String email) {
        Account accountToDel = getAccountRef(account, email);

        if (accountToDel.getBalance() != 0.0) {
            throw new IllegalArgumentException("Account to be deleted " + accountToDel + " must have zero balance");
        }
        users.get(email).getAccounts().remove(accountToDel);
        accounts.remove(account);
    }

    public void createCard(String account, String email, boolean oneTimeUse) {
        Account refAccount = getAccountRef(account, email);

        Card createdCard = refAccount.createCard(oneTimeUse);
        cards.put(createdCard.getNumber(), createdCard);
    }

    public void deleteCard(String cardNumber) {
        Card cardToDel = cards.get(cardNumber);
        if (cardToDel == null) {
            throw new IllegalArgumentException("Card " + cardNumber + " doesn't exist");
        }
        cardToDel.markAsDeleted();
        cards.remove(cardNumber);
    }
}
