package org.poo.bank.accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Card;
import org.poo.bank.exceptions.NotEnoughFundsException;
import org.poo.utils.Utils;

import java.util.LinkedHashSet;

@Getter
public class Account {
    private final String iban;
    private double balance;
    private final String ownerEmail;
    private final String currency;
    private final LinkedHashSet<Card> cards;

    @Setter
    private double minimumBalance;

    public Account(String ownerEmail, String currency) {
        this.iban = Utils.generateIBAN();
        this.balance = 0.0;
        this.ownerEmail = ownerEmail;
        this.currency = currency;

        this.cards = new LinkedHashSet<>();
        this.minimumBalance = 0;
    }

    public void addFunds(double amount) {
        balance += amount;
    }

    public void decreaseFunds(double amount) {
        if (balance < amount) {
            throw new NotEnoughFundsException(this);
        }
        balance -= amount;
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

    public void changeInterestRate(double interestRate, int timestamp) { }

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
