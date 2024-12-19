package org.poo.bank.accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Card;
import org.poo.exceptions.NotEnoughFundsException;
import org.poo.exceptions.NotSavingsAccountException;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.TreeMap;

@Getter
public class Account {
    private final String iban;
    private double balance;
    private final String ownerEmail;
    private final String currency;

    private final ArrayList<Card> cards;
    private final ArrayList<Transaction> transactions;


    /**
     * Represents the amount and the timestamp of a payment to a commerciant
     */
    @Getter
    public static class CommerciantPaymentData {
        private final double amount;
        private final int timestamp;

        public CommerciantPaymentData(final double amount, final int timestamp) {
            this.amount = amount;
            this.timestamp = timestamp;
        }
    }
    private final TreeMap<String, ArrayList<CommerciantPaymentData>> commerciants;

    @Setter
    private double minimumBalance;

    /**
     * Create a new classic account
     * @param ownerEmail the email of the owner
     * @param currency the currency
     */
    public Account(final String ownerEmail, final String currency) {
        this.iban = Utils.generateIBAN();
        this.balance = 0.0;
        this.ownerEmail = ownerEmail;
        this.currency = currency;

        this.cards = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.commerciants = new TreeMap<>();
        this.minimumBalance = 0;
    }

    /**
     * Add money to the account
     * @param amount how much to add
     */
    public void addFunds(final double amount) {
        balance += amount;
    }

    /**
     * Check if the account contains enough funds to make a payment
     * @param amount the amount balance has to be greater than or equal
     * @return whether the account has enough funds or not
     */
    public boolean ableToPaySum(final double amount) {
        return balance >= amount;
    }

    /**
     * Subtract money from the account
     * @param amount the amount to decrease by the balance of the account
     */
    public void decreaseFunds(final double amount) {
        if (ableToPaySum(amount)) {
            balance -= amount;
        } else {
            throw new NotEnoughFundsException(this);
        }
    }

    /**
     * Add a transaction to the list of transactions made by the account
     * @param transaction the transaction to add
     */
    public void addTransaction(final Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * Add a commerciant to the list of commerciants that the account made payments to
     * @param commerciant the name of the commerciant
     * @param amount the amount paid
     * @param timestamp the timestamp
     */
    public void addCommerciantPayment(final String commerciant,
                                      final double amount, final int timestamp) {
        if (!commerciants.containsKey(commerciant)) {
            commerciants.put(commerciant, new ArrayList<>());
        }
        commerciants.get(commerciant).add(new CommerciantPaymentData(amount, timestamp));
    }

    /**
     * Add a card to the list of cards associated to the account
     * @param card the card to add
     */
    public void addCard(final Card card) {
        cards.add(card);
    }

    /**
     * Remove a card from the list of cards associated to the account
     * @param card the card to remove
     */
    public void deleteCard(final Card card) {
        cards.remove(card);
    }

    /**
     * @return the type of the account
     */
    public String getType() {
        return AccountType.CLASSIC.getString();
    }

    /**
     * Add interest to the account.
     * This option is available only for savings accounts.
     * @return the added interest
     */
    public double addInterest() {
        throw new NotSavingsAccountException(this);
    }

    /**
     * Change the interest rate of the account.
     * @param interestRate the new interest rate
     */
    public void changeInterestRate(final double interestRate) {
        throw new NotSavingsAccountException(this);
    }

    /**
     * Generate a report of the transactions in a given interval
     * @param mapper the ObjectMapper to use
     * @param startTimestamp the start timestamp contained in the report
     * @param endTimestamp the end timestamp contained in the report
     * @return the report contents as an ObjectNode
     */
    public ObjectNode getReport(final ObjectMapper mapper,
                                final int startTimestamp, final int endTimestamp) {
        ObjectNode outputNode = mapper.createObjectNode();

        outputNode.put("IBAN", iban);
        outputNode.put("balance", balance);
        outputNode.put("currency", currency);

        ArrayNode transactionsNode = mapper.createArrayNode();
        for (Transaction transaction : transactions) {
            if (transaction.getTimestamp() < startTimestamp) {
                continue;
            } else if (transaction.getTimestamp() > endTimestamp) {
                break;
            }
            transactionsNode.add(transaction.toJson(mapper));
        }
        outputNode.set("transactions", transactionsNode);

        return outputNode;
    }

    /**
     * Generate a spending report of the account in a given interval, containing the list of
     * payments to the involved commerciants, and how much was paid to each commerciant
     * @param mapper the ObjectMapper to use
     * @param startTimestamp the start timestamp contained in the report
     * @param endTimestamp the end timestamp contained in the report
     * @return the report contents as an ObjectNode
     */
    public ObjectNode getSpendingReport(final ObjectMapper mapper,
                                        final int startTimestamp, final int endTimestamp) {
        ObjectNode outputNode = mapper.createObjectNode();

        outputNode.put("IBAN", iban);
        outputNode.put("balance", balance);
        outputNode.put("currency", currency);

        ArrayNode transactionsNode = mapper.createArrayNode();
        for (Transaction transaction : transactions) {
            if (transaction.getTimestamp() < startTimestamp) {
                continue;
            } else if (transaction.getTimestamp() > endTimestamp) {
                break;
            }
            if (transaction.displayedInSpendingReports()) {
                transactionsNode.add(transaction.toJson(mapper));
            }
        }
        outputNode.set("transactions", transactionsNode);

        ArrayNode commerciantsNode = mapper.createArrayNode();
        for (String commerciant : commerciants.keySet()) {
            double totalAmount = 0.0;
            for (CommerciantPaymentData currentPayment : commerciants.get(commerciant)) {
                if (currentPayment.getTimestamp() < startTimestamp) {
                    continue;
                } else if (currentPayment.getTimestamp() > endTimestamp) {
                    break;
                }
                totalAmount += currentPayment.getAmount();
            }
            if (totalAmount != 0.0) {
                ObjectNode currentNode = mapper.createObjectNode();
                currentNode.put("commerciant", commerciant);
                currentNode.put("total", totalAmount);

                commerciantsNode.add(currentNode);
            }
        }
        outputNode.set("commerciants", commerciantsNode);

        return outputNode;
    }

    /**
     * Get the data of the account in JSON format
     * @param mapper the ObjectMapper to use
     * @return the data of the account, as an ObjectNode
     */
    public ObjectNode toJSON(final ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("IBAN", iban);
        result.put("balance", balance);
        result.put("currency", currency);
        result.put("type", getType());

        ArrayNode cardsNode = mapper.createArrayNode();
        for (Card card : cards) {
            cardsNode.add(card.toJSON(mapper));
        }
        result.set("cards", cardsNode);

        return result;
    }
}
