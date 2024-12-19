package org.poo.bank.accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Card;
import org.poo.bank.accounts.transactions.Transaction;
import org.poo.bank.exceptions.NotEnoughFundsException;
import org.poo.bank.exceptions.NotSavingsAccountException;
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

    @Getter
    public static class CommerciantPayments {
        private final double amount;
        private final int timestamp;

        public CommerciantPayments(double amount, int timestamp) {
            this.amount = amount;
            this.timestamp = timestamp;
        }
    }
    private final TreeMap<String, ArrayList<CommerciantPayments>> commerciants;

    @Setter
    private double minimumBalance;

    public Account(String ownerEmail, String currency) {
        this.iban = Utils.generateIBAN();
        this.balance = 0.0;
        this.ownerEmail = ownerEmail;
        this.currency = currency;

        this.cards = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.commerciants = new TreeMap<>();
        this.minimumBalance = 0;
    }

    public void addFunds(double amount) {
        balance += amount;
    }

    public boolean ableToPaySum(double amount) {
        return balance >= amount;
    }

    public void decreaseFunds(double amount) {
        if (ableToPaySum(amount)) {
            balance -= amount;
        } else {
            throw new NotEnoughFundsException(this);
        }
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void addCommerciant(String commerciant, double amount, int timestamp) {
        if (!commerciants.containsKey(commerciant)) {
            commerciants.put(commerciant, new ArrayList<>());
        }
        commerciants.get(commerciant).add(new CommerciantPayments(amount, timestamp));
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void deleteCard(Card card) {
        cards.remove(card);
    }

    public String getType() {
        return AccountType.CLASSIC.getString();
    }

    public double addInterest() {
        throw new NotSavingsAccountException(this);
    }

    public void changeInterestRate(double interestRate) {
        throw new NotSavingsAccountException(this);
    }

    public ObjectNode getReport(ObjectMapper mapper, int startTimestamp, int endTimestamp) {
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

    public ObjectNode getSpendingReport(ObjectMapper mapper, int startTimestamp, int endTimestamp) {
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
            for (Account.CommerciantPayments currentPayment : commerciants.get(commerciant)) {
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

    public ObjectNode toJSON(ObjectMapper mapper) {
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
