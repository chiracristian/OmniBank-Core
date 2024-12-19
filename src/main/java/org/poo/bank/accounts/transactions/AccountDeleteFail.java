package org.poo.bank.accounts.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AccountDeleteFail extends Transaction {
    public AccountDeleteFail(int timestamp) {
        super(timestamp);
    }

    @Override
    public ObjectNode toJson(ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("timestamp", timestamp);
        result.put("description", "Account couldn't be deleted - there are funds remaining");

        return result;
    }
}
