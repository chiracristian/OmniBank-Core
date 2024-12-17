package org.poo.bank.accounts.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ReachedMinimumFunds implements Transaction {
    private final int timestamp;

    public ReachedMinimumFunds(int timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public ObjectNode toJson(ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("timestamp", timestamp);
        result.put("description", "You have reached the minimum amount of funds, the card will be frozen");

        return result;
    }
}
