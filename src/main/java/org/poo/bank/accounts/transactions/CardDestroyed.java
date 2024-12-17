package org.poo.bank.accounts.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CardDestroyed implements Transaction {
    private final int timestamp;
    private final String card;
    private final String cardHolder;
    private final String account;

    public CardDestroyed(int timestamp, String card, String cardHolder, String account) {
        this.timestamp = timestamp;
        this.card = card;
        this.cardHolder = cardHolder;
        this.account = account;
    }

    @Override
    public ObjectNode toJson(ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("timestamp", timestamp);
        result.put("description", "The card has been destroyed");
        result.put("card", card);
        result.put("cardHolder", cardHolder);
        result.put("account", account);

        return result;
    }
}
