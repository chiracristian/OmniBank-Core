package org.poo.bank.accounts.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AddedInterest extends Transaction {
    private final double amount;
    public AddedInterest(int timestamp, double amount) {
        super(timestamp);
        this.amount = amount;
    }

    @Override
    public boolean displayedInSavingAccountsReports() {
        return true;
    }

    @Override
    public ObjectNode toJson(ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("timestamp", timestamp);
        result.put("description", "Paid interest");
        result.put("amount", amount);

        return result;
    }
}
