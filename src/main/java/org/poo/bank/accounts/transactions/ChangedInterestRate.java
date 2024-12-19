package org.poo.bank.accounts.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ChangedInterestRate extends Transaction {
    private final double interestRate;
    public ChangedInterestRate(int timestamp, double interestRate) {
        super(timestamp);
        this.interestRate = interestRate;
    }

    @Override
    public boolean displayedInSavingAccountsReports() {
        return true;
    }

    @Override
    public ObjectNode toJson(ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("timestamp", timestamp);
        result.put("description", "Interest rate of the account changed to " + interestRate);

        return result;
    }
}
