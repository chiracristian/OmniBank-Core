package org.poo.bank;

import lombok.Getter;
import org.poo.bank.accounts.Account;
import org.poo.exceptions.AccountEmailMismatchException;

import org.poo.exceptions.NonExistingIbanException;
import org.poo.exceptions.NonExistingUserException;
import org.poo.exceptions.NotZeroFundsException;
import org.poo.exceptions.NonExistingCardException;

import org.poo.transactions.*;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.List;

public final class Bank {
    // Users, referenced by their email
    @Getter
    private final LinkedHashMap<String, User> users;

    // Accounts, referenced by their IBAN
    private final HashMap<String, Account> accounts;

    // IBANs, referenced by aliases
    private final HashMap<String, String> aliases;

    // Cards, referenced by their number
    private final HashMap<String, Card> cards;

    private final CurrencyExchanger currencyExchanger;

    /**
     * Construct a new bank
     * @param input the input data
     */
    public Bank(final ObjectInput input) {
        users = new LinkedHashMap<>();

        for (UserInput userIn : input.getUsers()) {
            users.put(userIn.getEmail(), new User(userIn));
        }

        accounts = new HashMap<>();
        aliases = new HashMap<>();
        cards = new HashMap<>();

        currencyExchanger = new CurrencyExchanger(input.getExchangeRates());
    }

    /**
     * @param email the email of the user
     * @return the user that has the given email
     */
    public User getUserByEmail(final String email) {
        User result = users.get(email);
        if (result == null) {
            throw new NonExistingUserException(email);
        }
        return result;
    }

    /**
     * @param account the referenced account
     * @return the user that owns the given account
     */
    public User getUserByAccount(final Account account) {
        return getUserByEmail(account.getOwnerEmail());
    }

    /**
     * @param iban the IBAN of the account
     * @return a reference to the Account that has the given IBAN
     */
    public Account getAccountByIban(final String iban) {
        Account result = accounts.get(iban);
        if (result == null) {
            throw new NonExistingIbanException(iban);
        }
        return result;
    }

    /**
     * @param aliasOrIban the alias or the IBAN of the account
     * @return a reference to the Account that has the given alias or IBAN
     */
    private Account getAccountByAliasOrIban(final String aliasOrIban) {
        Account result = accounts.get(aliases.get(aliasOrIban));
        if (result == null) {
            return getAccountByIban(aliasOrIban);
        }
        return result;
    }

    /**
     * Check if an account is owned by a user with the given email.
     * Throw AccountEmailMismatchException in case of mismatch
     * @param account the referenced account
     * @param email the email that should own the account
     */
    private void checkEmailAccountMatch(final Account account, final String email) {
        if (!account.getOwnerEmail().equals(email)) {
            throw new AccountEmailMismatchException(email, account.getIban());
        }
    }

    /**
     * Add a new account
     * @param addedAccount the added account
     * @param ownerEmail the email of the owner
     * @param timestamp the timestamp of the operation
     */
    public void addAccount(final Account addedAccount, final String ownerEmail,
                           final int timestamp) {
        accounts.put(addedAccount.getIban(), addedAccount);
        getUserByEmail(ownerEmail).addAccount(addedAccount);

        Transaction accountCreated = new AccountCreated(timestamp);
        addTransaction(addedAccount, accountCreated);
    }

    /**
     * Delete an account. The account must have the balance equal zero, otherwise
     * NotZeroFundsException will be thrown
     * @param iban the IBAN of the account
     * @param email the email of the owner
     * @param timestamp the timestamp of the operation
     */
    public void deleteAccount(final String iban, final String email, final int timestamp) {
        Account accountToDel = getAccountByIban(iban);
        checkEmailAccountMatch(accountToDel, email);

        if (accountToDel.getBalance() != 0.0) {
            Transaction deleteFail = new AccountDeleteFail(timestamp);
            addTransaction(accountToDel, deleteFail);

            throw new NotZeroFundsException(accountToDel);
        }

        getUserByEmail(email).getAccounts().remove(accountToDel);
        accounts.remove(iban);
    }

    /**
     * Change the interest rate of an account
     * @param iban the IBAN of the account
     * @param interestRate the new interest rate
     * @param timestamp the timestamp of the operation
     */
    public void changeInterestRate(final String iban, final double interestRate,
                                   final int timestamp) {
        Account refAccount = getAccountByIban(iban);
        refAccount.changeInterestRate(interestRate);

        Transaction changedInterest = new ChangedInterestRate(timestamp, interestRate);
        addTransaction(refAccount, changedInterest);
    }

    /**
     * Add interest to an account
     * @param iban the IBAN of the account
     * @param timestamp the timestamp of the operation
     */
    public void addInterest(final String iban, final int timestamp) {
        Account refAccount = getAccountByIban(iban);
        double addedAmount = refAccount.addInterest();

        Transaction addedInterest = new AddedInterest(timestamp, addedAmount);
        addTransaction(refAccount, addedInterest);
    }

    /**
     * Add a transaction to an account
     * @param account the IBAN of the account
     * @param transaction the transaction to add
     */
    public void addTransaction(final Account account, final Transaction transaction) {
        account.addTransaction(transaction);
        getUserByAccount(account).addTransaction(transaction);
    }

    /**
     * Create a new card
     * @param iban the IBAN of the account the card should belong to
     * @param email the email of the owner of the account
     * @param oneTimeUse whether the card is one-time use or not
     * @param timestamp the timestamp of the operation
     * @return a reference to the created Card
     */
    public Card createCard(final String iban, final String email,
                           final boolean oneTimeUse, final int timestamp) {
        Account refAccount = getAccountByIban(iban);
        checkEmailAccountMatch(refAccount, email);

        Card createdCard = new Card(refAccount, oneTimeUse);
        refAccount.addCard(createdCard);

        String cardNumber = createdCard.getNumber();
        cards.put(cardNumber, createdCard);

        Transaction createdCardTransaction = new CardCreated(timestamp, cardNumber, email, iban);
        addTransaction(refAccount, createdCardTransaction);

        return createdCard;
    }

    /**
     * @param cardNumber the number of the card
     * @return a reference to the Card
     */
    public Card getCard(final String cardNumber) {
        Card result = cards.get(cardNumber);
        if (result == null) {
            throw new NonExistingCardException(cardNumber);
        }
        return result;
    }

    /**
     * Delete a card
     * @param email the email of the user that owns the card
     * @param cardNumber the number of the card to delete
     * @param timestamp the timestamp of the operation
     */
    public void deleteCard(final String email, final String cardNumber, final int timestamp) {
        Card cardToDel = getCard(cardNumber);

        Account refAccount = getAccountByIban(cardToDel.getAssociatedAccount().getIban());
        checkEmailAccountMatch(refAccount, email);

        Transaction destroyedTransaction = new CardDestroyed(timestamp, cardNumber,
                email, refAccount.getIban());
        addTransaction(refAccount, destroyedTransaction);

        cards.remove(cardToDel.getNumber());
        cardToDel.getAssociatedAccount().deleteCard(cardToDel);
    }

    /**
     * Update the status of a card
     * @param cardNumber the number of the card
     * @param timestamp the timestamp of the operation
     */
    public void checkCardStatus(final String cardNumber, final int timestamp) {
        Card refCard = cards.get(cardNumber);
        if (refCard == null) {
            throw new NonExistingCardException(cardNumber);
        }
        Account refAccount = refCard.getAssociatedAccount();

        Card.Status prevStatus = refCard.getStatus();
        refCard.updateStatus();
        if (prevStatus != refCard.getStatus()) {
            if (refCard.getStatus() == Card.Status.FROZEN) {
                Transaction reachedMinimum = new ReachedMinimumFunds(timestamp);
                addTransaction(refAccount, reachedMinimum);
            }
        }
    }

    /**
     * Make a card online payment
     * @param cardNumber the number of the card used for the payment
     * @param amount the amount to pay
     * @param currency the currency in which the payment is made
     * @param timestamp the timestamp of the operation
     * @param commerciant the commerciant to be paid
     * @param email the email of the user that owns the card used for the payment
     */
    public void payOnline(final String cardNumber, final double amount, final String currency,
                          final int timestamp, final String commerciant, final String email) {
        Card cardUsed = getCard(cardNumber);

        Account refAccount = getAccountByIban(cardUsed.getAssociatedAccount().getIban());
        checkEmailAccountMatch(refAccount, email);

        if (cardUsed.getStatus() == Card.Status.FROZEN) {
            Transaction frozenCardTransaction = new CardIsFrozen(timestamp);
            addTransaction(refAccount, frozenCardTransaction);
            return;
        }

        double convertedAmount = currencyExchanger.convert(amount,
                currency, refAccount.getCurrency());

        if (refAccount.ableToPaySum(convertedAmount)) {
            refAccount.decreaseFunds(convertedAmount);
        } else {
            Transaction insufficientFundsTransaction = new InsufficientFunds(timestamp);
            addTransaction(refAccount, insufficientFundsTransaction);
            return;
        }

        refAccount.addCommerciant(commerciant, convertedAmount, timestamp);

        Transaction successTransaction = new CardPayment(timestamp, convertedAmount, commerciant);
        addTransaction(refAccount, successTransaction);

        if (cardUsed.isOneTimeUse()) {
            deleteCard(email, cardNumber, timestamp);
            createCard(refAccount.getIban(), email, true, timestamp);
        }
    }

    /**
     * Create an alias for a given account
     * @param email the email of the account's owner
     * @param iban the IBAN of user's account
     * @param alias the desired alias
     */
    public void setAlias(final String email, final String iban, final String alias) {
        Account accountRef = getAccountByIban(iban);
        checkEmailAccountMatch(accountRef, email);

        aliases.put(alias, iban);
    }

    /**
     * Make a funds transfer between two accounts
     * @param fromIban the IBAN of the sender account
     * @param amount the amount to send (in sender's currency)
     * @param toAliasOrIban the alias or IBAN of the receiver account
     * @param timestamp the timestamp of the operation
     * @param email the email of the sender user
     * @param description the description of the payment
     */
    public void sendMoney(final String fromIban, final double amount, final String toAliasOrIban,
                          final int timestamp, final String email, final String description) {
        Account fromAccount = getAccountByIban(fromIban);
        checkEmailAccountMatch(fromAccount, email);

        Account toAccount = getAccountByAliasOrIban(toAliasOrIban);

        if (fromAccount.ableToPaySum(amount)) {
            fromAccount.decreaseFunds(amount);
        } else {
            Transaction insufficientFundsTransaction = new InsufficientFunds(timestamp);
            addTransaction(fromAccount, insufficientFundsTransaction);
            return;
        }

        double convertedAmount = currencyExchanger.convert(amount,
                fromAccount.getCurrency(), toAccount.getCurrency());
        toAccount.addFunds(convertedAmount);

        Transaction sentTransaction = new TransferPayment(timestamp, description, fromIban,
                toAliasOrIban, amount, fromAccount.getCurrency(), TransferPayment.Type.SENT);
        addTransaction(fromAccount, sentTransaction);

        Transaction receivedTransaction = new TransferPayment(timestamp, description, fromIban,
                toAliasOrIban, convertedAmount, toAccount.getCurrency(),
                TransferPayment.Type.RECEIVED);
        addTransaction(toAccount, receivedTransaction);
    }

    /**
     * Make a payment shared across multiple accounts
     * @param ibanList the list of IBANs of the involved accounts
     * @param timestamp the timestamp of the operation
     * @param currency the currency in which the payment is made
     * @param amount the amount to pay
     */
    public void splitPayment(final List<String> ibanList, final int timestamp,
                             final String currency, final double amount) {
        ArrayList<Account> fromAccounts = new ArrayList<>(ibanList.size());
        for (String currentIban : ibanList) {
            fromAccounts.add(getAccountByIban(currentIban));
        }

        double amountPerPerson = amount / fromAccounts.size();
        ArrayList<Double> sumsPaid = new ArrayList<>(fromAccounts.size());

        Account accountWithInsufficientFunds = null;
        for (Account account : fromAccounts) {
            double convertedAmount = currencyExchanger.convert(amountPerPerson,
                    currency, account.getCurrency());

            if (!account.ableToPaySum(convertedAmount)) {
                accountWithInsufficientFunds = account;
            }
            sumsPaid.add(convertedAmount);
        }

        for (int i = 0; i < fromAccounts.size(); i++) {
            SplitPayment splitPayment;
            if (accountWithInsufficientFunds == null) {
                fromAccounts.get(i).decreaseFunds(sumsPaid.get(i));
                splitPayment = new SplitPayment(timestamp, currency, amountPerPerson,
                        amount, ibanList);
            } else {
                splitPayment = new SplitPayment(timestamp, currency, amountPerPerson,
                        amount, ibanList, accountWithInsufficientFunds.getIban());
            }
            addTransaction(fromAccounts.get(i), splitPayment);
        }
    }
}
