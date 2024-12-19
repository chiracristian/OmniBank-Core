package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class InsufficientFunds extends Transaction {
    public InsufficientFunds(final int timestamp) {
        super(timestamp);
    }

    @Override
    public ObjectNode toJson(final ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("timestamp", timestamp);
        result.put("description", "Insufficient funds");

        return result;
    }
}
