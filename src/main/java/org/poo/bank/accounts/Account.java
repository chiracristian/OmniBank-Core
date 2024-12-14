package org.poo.bank.accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Card;
import org.poo.utils.Utils;

import java.util.ArrayList;

@Getter
public class Account {
    private final String iban;
    private double balance;
    private final String currency;
    private final ArrayList<Card> cards;

    @Setter
    private double minimumBalance;

    public Account(String currency) {
        this.iban = Utils.generateIBAN();
        this.balance = 0.0;
        this.currency = currency;
        this.cards = new ArrayList<>();

        this.minimumBalance = 0;
    }

    public void addFunds(double amount) {
        balance += amount;
    }

    public void decreaseFunds(double amount) {
        balance -= amount;
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
        result.put("currency", currency);
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
