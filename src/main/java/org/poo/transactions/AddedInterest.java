package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class AddedInterest extends Transaction {
    private final double amount;
    public AddedInterest(final int timestamp, final double amount) {
        super(timestamp);
        this.amount = amount;
    }

    @Override
    public boolean displayedInSavingAccountsReports() {
        return true;
    }

    @Override
    public ObjectNode toJson(final ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("timestamp", timestamp);
        result.put("description", "Paid interest");
        result.put("amount", amount);

        return result;
    }
}
