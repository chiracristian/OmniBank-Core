package org.poo.bank.accounts.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.Locale;

public class SplitPayment extends Transaction {
    private final String currency;
    private final double amountPaid;
    private final List<String> involvedAccounts;
    private final String accountWithNotEnough;

    private final String description;

    public SplitPayment(int timestamp, String currency, double amountPaid, double totalAmount,
                        List<String> involvedAccounts) {
        this(timestamp, currency, amountPaid, totalAmount, involvedAccounts, null);
    }

    public SplitPayment(int timestamp, String currency, double amountPaid, double totalAmount,
                        List<String> involvedAccounts, String accountWithNotEnough) {
        super(timestamp);
        this.currency = currency;
        this.amountPaid = amountPaid;
        this.involvedAccounts = involvedAccounts;
        this.accountWithNotEnough = accountWithNotEnough;

        String totalAmountDisplay = String.format(Locale.US, "%.2f", totalAmount);
        this.description = "Split payment of " + totalAmountDisplay + " " + currency;
    }

    @Override
    public ObjectNode toJson(ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("timestamp", timestamp);
        result.put("description", description);
        result.put("currency", currency);
        result.put("amount", amountPaid);

        ArrayNode accountsNode = mapper.createArrayNode();
        for (String currentAccount : involvedAccounts) {
            accountsNode.add(currentAccount);
        }
        result.set("involvedAccounts", accountsNode);

        if (accountWithNotEnough != null) {
            String errorMessage = "Account " + accountWithNotEnough + " has insufficient funds for a split payment.";
            result.put("error", errorMessage);
        }

        return result;
    }
}
