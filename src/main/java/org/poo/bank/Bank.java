package org.poo.bank;

import lombok.Getter;
import org.poo.bank.accounts.Account;
import org.poo.bank.accounts.transactions.*;
import org.poo.bank.exceptions.*;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;

import java.util.*;

public class Bank {
    /**
     * Users, referenced by their email
     */
    @Getter
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

    private final CurrencyManager currencyManager;

    public Bank(ObjectInput input) {
        users = new LinkedHashMap<>();

        for (UserInput userIn : input.getUsers()) {
            users.put(userIn.getEmail(), new User(userIn));
        }

        accounts = new HashMap<>();
        aliases = new HashMap<>();
        cards = new HashMap<>();

        currencyManager = new CurrencyManager(input.getExchangeRates());
    }

    public User getUserByEmail(String email) {
        User result = users.get(email);
        if (result == null) {
            throw new NonExistingUserException(email);
        }
        return result;
    }

    public User getUserByAccount(Account account) {
        return getUserByEmail(account.getOwnerEmail());
    }

    public Account getAccountByIban(String iban) {
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

    public void addAccount(Account addedAccount, String ownerEmail, int timestamp) {
        accounts.put(addedAccount.getIban(), addedAccount);
        getUserByEmail(ownerEmail).addAccount(addedAccount);

        Transaction accountCreated = new AccountCreated(timestamp);
        addTransaction(addedAccount, accountCreated);
    }

    public void deleteAccount(String iban, String email, int timestamp) {
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

    public void changeInterestRate(String iban, double interestRate, int timestamp) {
        Account refAccount = getAccountByIban(iban);
        refAccount.changeInterestRate(interestRate);

        Transaction changedInterest = new ChangedInterestRate(timestamp, interestRate);
        addTransaction(refAccount, changedInterest);
    }

    public void addInterest(String iban, int timestamp) {
        Account refAccount = getAccountByIban(iban);
        double addedAmount = refAccount.addInterest();

        Transaction addedInterest = new AddedInterest(timestamp, addedAmount);
        addTransaction(refAccount, addedInterest);
    }

    public void addTransaction(Account account, Transaction transaction) {
        account.addTransaction(transaction);
        getUserByAccount(account).addTransaction(transaction);
    }

    public Card createCard(String iban, String email, boolean oneTimeUse, int timestamp) {
        Account refAccount = getAccountByIban(iban);
        checkEmailAccountMatch(refAccount, email);

        Card createdCard = new Card(refAccount, oneTimeUse);

        refAccount.addCard(createdCard);
        cards.put(createdCard.getNumber(), createdCard);

        Transaction createdCardTransaction = new CardCreated(timestamp, createdCard.getNumber(), email, iban);
        addTransaction(refAccount, createdCardTransaction);

        return createdCard;
    }

    public Card getCard(String cardNumber) {
        Card result = cards.get(cardNumber);
        if (result == null) {
            throw new NonExistingCardException(cardNumber);
        }
        return result;
    }

    public void deleteCard(String email, String cardNumber, int timestamp) {
        Card cardToDel = getCard(cardNumber);

        Account refAccount = getAccountByIban(cardToDel.getAssociatedAccount().getIban());
        checkEmailAccountMatch(refAccount, email);

        Transaction destroyedTransaction = new CardDestroyed(timestamp, cardNumber, email, refAccount.getIban());
        addTransaction(refAccount, destroyedTransaction);

        cards.remove(cardToDel.getNumber());
        cardToDel.getAssociatedAccount().deleteCard(cardToDel);
    }

    public void checkCardStatus(String cardNumber, int timestamp) {
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

    public void payOnline(String cardNumber, double amount, String currency, int timestamp,
                          String commerciant, String email) {
        Card cardUsed = getCard(cardNumber);

        Account refAccount = getAccountByIban(cardUsed.getAssociatedAccount().getIban());
        checkEmailAccountMatch(refAccount, email);

        if (cardUsed.getStatus() == Card.Status.FROZEN) {
            Transaction frozenCardTransaction = new CardIsFrozen(timestamp);
            addTransaction(refAccount, frozenCardTransaction);
            return;
        }

        double convertedAmount = currencyManager.convert(amount, currency, refAccount.getCurrency());

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

        if (fromAccount.ableToPaySum(amount)) {
            fromAccount.decreaseFunds(amount);
        } else {
            Transaction insufficientFundsTransaction = new InsufficientFunds(timestamp);
            addTransaction(fromAccount, insufficientFundsTransaction);
            return;
        }
        double convertedAmount = currencyManager.convert(amount, fromAccount.getCurrency(), toAccount.getCurrency());
        toAccount.addFunds(convertedAmount);

        Transaction sentTransaction = new TransferPayment(timestamp, description, fromIban, toAliasOrIban,
                amount, fromAccount.getCurrency(), TransferPayment.Type.SENT);
        addTransaction(fromAccount, sentTransaction);

        Transaction receivedTransaction = new TransferPayment(timestamp, description, fromIban, toAliasOrIban,
                convertedAmount, toAccount.getCurrency(), TransferPayment.Type.RECEIVED);
        addTransaction(toAccount, receivedTransaction);
    }

    public void splitPayment(List<String> ibanList, int timestamp, String currency, double amount) {
        ArrayList<Account> fromAccounts = new ArrayList<>(ibanList.size());
        for (String currentIban : ibanList) {
            fromAccounts.add(getAccountByIban(currentIban));
        }

        double amountPerPerson = amount / fromAccounts.size();
        ArrayList<Double> sumsPaid = new ArrayList<>(fromAccounts.size());

        Account accountWithInsufficientFunds = null;
        for (Account account : fromAccounts) {
            double convertedAmount = currencyManager.convert(amountPerPerson, currency, account.getCurrency());

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
