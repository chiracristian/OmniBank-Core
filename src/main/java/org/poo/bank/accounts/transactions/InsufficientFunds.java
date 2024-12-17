package org.poo.bank.accounts.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class InsufficientFunds implements Transaction {
    private final int timestamp;

    public InsufficientFunds(int timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public ObjectNode toJson(ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("timestamp", timestamp);
        result.put("description", "Insufficient funds");

        return result;
    }
}
