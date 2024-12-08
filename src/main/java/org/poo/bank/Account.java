package org.poo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class Account {
    private final String iban;
    private double balance;
    private final Currency currency;
    private final ArrayList<Card> cards;

    Account(Currency currency) {
        this.iban = Utils.generateIBAN();
        this.balance = 0.0;
        this.currency = currency;
        this.cards = new ArrayList<>();
    }

    public void addFunds(double amount) {
        balance += amount;
    }

    public Card createCard(boolean oneTimeUse) {
        Card createdCard = new Card(oneTimeUse);
        cards.add(createdCard);
        return createdCard;
    }

    public ObjectNode toJSON(ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("IBAN", iban);
        result.put("balance", balance);
        result.put("currency", currency.getName());
        result.put("type", AccountType.CLASSIC.getString());

        ArrayNode cardsNode = mapper.createArrayNode();
        for (Card card : cards) {
            if (!card.isDeleted()) {
                cardsNode.add(card.toJSON(mapper));
            }
        }
        result.set("cards", cardsNode);

        return result;
    }
}
