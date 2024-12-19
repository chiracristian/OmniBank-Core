package org.poo.bank.accounts.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CardPayment extends Transaction {
    private final double amount;
    private final String commerciant;

    public CardPayment(int timestamp, double amount, String commerciant) {
        super(timestamp);
        this.amount = amount;
        this.commerciant = commerciant;
    }

    @Override
    public boolean displayedInSpendingReports() {
        return true;
    }

    @Override
    public ObjectNode toJson(ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("timestamp", timestamp);
        result.put("description", "Card payment");
        result.put("amount", amount);
        result.put("commerciant", commerciant);

        return result;
    }
}
