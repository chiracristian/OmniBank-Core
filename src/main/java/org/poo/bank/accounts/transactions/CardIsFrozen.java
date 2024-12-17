package org.poo.bank.accounts.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CardIsFrozen implements Transaction {
    private final int timestamp;

    public CardIsFrozen(int timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public ObjectNode toJson(ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("timestamp", timestamp);
        result.put("description", "The card is frozen");

        return result;
    }
}
