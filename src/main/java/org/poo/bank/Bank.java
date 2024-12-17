package org.poo.bank;

import lombok.Getter;
import org.poo.bank.accounts.Account;
import org.poo.bank.accounts.transactions.*;
import org.poo.bank.exceptions.*;
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
     * Keys: aliases, Value: IBANs
     */
    private final HashMap<String, String> aliases;

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
        aliases = new HashMap<>();
        cards = new HashMap<>();
        commerciants = new ArrayList<>();

        if (input.getCommerciants() != null) {
            for (CommerciantInput commIn : input.getCommerciants()) {
                commerciants.add(new CommerciantCategory(commIn));
            }
        }

        currencyManager = new CurrencyManager(input.getExchangeRates());
    }

    private Account getAccountByIban(String iban) {
        Account result = accounts.get(iban);
        if (result == null) {
            throw new NonExistingIbanException(iban);
        }
        return result;
    }

    private Account getAccountByAliasOrIban(String aliasOrIban) {
        Account result = accounts.get(aliases.get(aliasOrIban));
        if (result == null) {
            return getAccountByIban(aliasOrIban);
        }
        return result;
    }

    private void checkEmailAccountMatch(Account account, String email) {
        if (!account.getOwnerEmail().equals(email)){
            throw new AccountEmailMismatchException(email, account.getIban());
        }
    }

    public void addAccount(Account addedAccount, String ownerEmail) {
        accounts.put(addedAccount.getIban(), addedAccount);
        users.get(ownerEmail).addAccount(addedAccount);
    }

    public void deleteAccount(String iban, String email) {
        Account accountToDel = getAccountByIban(iban);
        checkEmailAccountMatch(accountToDel, email);

        if (accountToDel.getBalance() != 0.0) {
            throw new NotZeroFundsException(accountToDel);
        }
        users.get(email).getAccounts().remove(accountToDel);
        accounts.remove(iban);
    }

    public Card createCard(String iban, String email, boolean oneTimeUse) {
        Account refAccount = getAccountByIban(iban);
        checkEmailAccountMatch(refAccount, email);

        Card createdCard = new Card(refAccount, oneTimeUse);
        //System.out.println("Created card " + createdCard.getNumber() + " one time? " + createdCard.isOneTimeUse());

        refAccount.addCard(createdCard);
        cards.put(createdCard.getNumber(), createdCard);

        return createdCard;
    }

    public void deleteCard(String email, String cardNumber, int timestamp) {
        Card cardToDel = cards.get(cardNumber);
        //System.out.println("Attempting to delete " + cardNumber);
        if (cardToDel == null) {
            //System.out.println("Doesn't exist\n");
            throw new NonExistingCardException(cardNumber);
        }

        Account refAccount = getAccountByIban(cardToDel.getAssociatedAccount().getIban());
        checkEmailAccountMatch(refAccount, email);

        Transaction destroyedTransaction = new CardDestroyed(timestamp, cardNumber, email, refAccount.getIban());
        users.get(refAccount.getOwnerEmail()).addTransaction(destroyedTransaction);

        cards.remove(cardToDel.getNumber());
        cardToDel.getAssociatedAccount().deleteCard(cardToDel);
    }

    private boolean decreaseFundsSafely(Account account, double amount, int timestamp) {
        try {
            account.decreaseFunds(amount);
            return true;
        } catch (NotEnoughFundsException notEnoughFunds) {
            Transaction insufficientFundsTransaction = new InsufficientFunds(timestamp);
            users.get(account.getOwnerEmail()).addTransaction(insufficientFundsTransaction);
            return false;
        }
    }

    public void payOnline(String cardNumber, double amount, String currency, int timestamp,
                          String description, String commerciant, String email) {
        Card cardUsed = cards.get(cardNumber);
        if (cardUsed == null) {
            //System.out.println("Doesn't exist card " + cardNumber + "\n");
            throw new NonExistingCardException(cardNumber);
        }
        //System.out.println("Paying with card " + cardNumber + " one time? " + cardUsed.isOneTimeUse() + "\n");
        Account refAccount = getAccountByIban(cardUsed.getAssociatedAccount().getIban());
        checkEmailAccountMatch(refAccount, email);

        double convertedAmount = currencyManager.convert(amount, currency, refAccount.getCurrency());
        if (!decreaseFundsSafely(refAccount, convertedAmount, timestamp)) {
            return;
        }

        if (cardUsed.isOneTimeUse()) {
            //deleteCard(cardNumber);
            createCard(refAccount.getIban(), email, true);
        }

        Transaction successTransaction = new CardPayment(timestamp, convertedAmount, commerciant);
        users.get(refAccount.getOwnerEmail()).addTransaction(successTransaction);
    }

    public void setAlias(String email, String iban, String alias) {
        Account accountRef = getAccountByIban(iban);
        checkEmailAccountMatch(accountRef, email);

        aliases.put(alias, iban);
    }

    public void sendMoney(String fromIban, double amount, String toAliasOrIban, int timestamp,
                          String email, String description) {
        Account fromAccount = getAccountByIban(fromIban);
        checkEmailAccountMatch(fromAccount, email);

        Account toAccount = getAccountByAliasOrIban(toAliasOrIban);

        if (!decreaseFundsSafely(fromAccount, amount, timestamp)) {
            return;
        }
        toAccount.addFunds(currencyManager.convert(amount, fromAccount.getCurrency(), toAccount.getCurrency()));

        Transaction successTransaction = new TransferPayment(timestamp, description, fromIban, toAliasOrIban,
                amount, fromAccount.getCurrency());
        users.get(fromAccount.getOwnerEmail()).addTransaction(successTransaction);
    }
}
