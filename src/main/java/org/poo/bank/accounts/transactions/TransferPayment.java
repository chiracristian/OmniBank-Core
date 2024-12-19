package org.poo.bank.accounts.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;

public class TransferPayment extends Transaction {
    private final String description;
    private final String senderIban;
    private final String receiverIban;
    private final double amount;
    private final String currency;

    @Getter
    public enum Type {
        SENT("sent"),
        RECEIVED("received");

        private final String string;

        Type(String string) {
            this.string = string;
        }
    }
    private final Type type;

    public TransferPayment(int timestamp, String description, String senderIban,
                           String receiverIban, double amount, String currency, Type type) {
        super(timestamp);
        this.description = description;
        this.senderIban = senderIban;
        this.receiverIban = receiverIban;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
    }

    @Override
    public ObjectNode toJson(ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("timestamp", timestamp);
        result.put("description", description);
        result.put("senderIBAN", senderIban);
        result.put("receiverIBAN", receiverIban);
        result.put("amount", amount + " " + currency);
        result.put("transferType", type.getString());

        return result;
    }
}
