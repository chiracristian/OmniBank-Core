package org.poo.bank.accounts.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class InsufficientFunds extends Transaction {
    public InsufficientFunds(int timestamp) {
        super(timestamp);
    }

    @Override
    public ObjectNode toJson(ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("timestamp", timestamp);
        result.put("description", "Insufficient funds");

        return result;
    }
}
