package org.poo.bank;

import lombok.Getter;
import org.poo.bank.accounts.Account;
import org.poo.fileio.CommerciantInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Getter
public class Bank {
    /**
     * Users, referenced by their email
     */
    private final LinkedHashMap<String, User> users;

    /**
     * Accounts, referenced by their IBAN
     */
    private final HashMap<String, Account> accounts;

    /**
     * Cards, referenced by their number
     */
    private final HashMap<String, Card> cards;

    private final ArrayList<CommerciantCategory> commerciants;

    private final CurrencyManager currencyManager;

    public Bank(ObjectInput input) {
        users = new LinkedHashMap<>();

        for (UserInput userIn : input.getUsers()) {
            users.put(userIn.getEmail(), new User(userIn));
        }

        accounts = new HashMap<>();
        cards = new HashMap<>();
        commerciants = new ArrayList<>();

        if (input.getCommerciants() != null) {
            for (CommerciantInput commIn : input.getCommerciants()) {
                commerciants.add(new CommerciantCategory(commIn));
            }
        }

        currencyManager = new CurrencyManager(input.getExchangeRates());
    }

    private Account getAccountRef(String iban, String email) {
        Account refAccount = accounts.get(iban);
        if (refAccount == null) {
            throw new IllegalArgumentException("Account " + iban + " doesn't exist!");
        }
        if (!users.get(email).getAccounts().contains(refAccount)) {
            throw new IllegalArgumentException("Email of account " + iban + " is not " + email);
        }

        return refAccount;
    }

    public void addAccount(Account addedAccount, String ownerEmail) {
        accounts.put(addedAccount.getIban(), addedAccount);
        users.get(ownerEmail).addAccount(addedAccount);
    }

    public void deleteAccount(String iban, String email) {
        Account accountToDel = getAccountRef(iban, email);

        if (accountToDel.getBalance() != 0.0) {
            throw new IllegalArgumentException("Account " + accountToDel + " still has funds.");
        }
        users.get(email).getAccounts().remove(accountToDel);
        accounts.remove(iban);
    }

    public void createCard(String iban, String email, boolean oneTimeUse) {
        Account refAccount = getAccountRef(iban, email);
        Card createdCard = new Card(refAccount, oneTimeUse);

        refAccount.addCard(createdCard);
        cards.put(createdCard.getNumber(), createdCard);
    }

    public void deleteCard(String cardNumber) {
        Card cardToDel = cards.get(cardNumber);
        if (cardToDel == null) {
            throw new NonExistingCardException(cardNumber);
        }

        cardToDel.getAssociatedAccount().deleteCard(cardToDel);
        cards.remove(cardNumber);
    }

    public void payOnline(String cardNumber, double amount, String currency, String description,
                          String commerciant, String email) {
        Card cardUsed = cards.get(cardNumber);
        if (cardUsed == null) {
            throw new NonExistingCardException(cardNumber);
        }
        Account refAccount = getAccountRef(cardUsed.getAssociatedAccount().getIban(), email);

        refAccount.decreaseFunds(currencyManager.convert(amount, currency, refAccount.getCurrency()));

        if (cardUsed.isOneTimeUse()) {
            refAccount.deleteCard(cardUsed);
            createCard(refAccount.getIban(), email, true);
        }
    }

    public void sendMoney(String fromIban, double amount, String toIban, String description) {
        Account fromAccount = accounts.get(fromIban);
        Account toAccount = accounts.get(toIban);

        fromAccount.decreaseFunds(amount);
        toAccount.addFunds(currencyManager.convert(amount, fromAccount.getCurrency(), toAccount.getCurrency()));
    }
}
