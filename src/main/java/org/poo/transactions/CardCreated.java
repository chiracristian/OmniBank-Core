package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class CardCreated extends Transaction {
    private final String card;
    private final String cardHolder;
    private final String account;

    public CardCreated(final int timestamp, final String card, final String cardHolder,
                       final String account) {
        super(timestamp);
        this.card = card;
        this.cardHolder = cardHolder;
        this.account = account;
    }

    @Override
    public ObjectNode toJson(final ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("timestamp", timestamp);
        result.put("description", "New card created");
        result.put("card", card);
        result.put("cardHolder", cardHolder);
        result.put("account", account);

        return result;
    }
}
