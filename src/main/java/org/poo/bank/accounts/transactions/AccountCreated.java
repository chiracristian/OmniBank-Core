package org.poo.bank.accounts.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AccountCreated extends Transaction {
    public AccountCreated(int timestamp) {
        super(timestamp);
    }

    @Override
    public ObjectNode toJson(ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("timestamp", timestamp);
        result.put("description", "New account created");

        return result;
    }
}
