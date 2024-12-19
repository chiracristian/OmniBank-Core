package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;

public final class TransferPayment extends Transaction {
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

        Type(final String string) {
            this.string = string;
        }
    }
    private final Type type;

    public TransferPayment(final int timestamp, final String description, final String senderIban,
                           final String receiverIban, final double amount, final String currency,
                           final Type type) {
        super(timestamp);
        this.description = description;
        this.senderIban = senderIban;
        this.receiverIban = receiverIban;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
    }

    @Override
    public ObjectNode toJson(final ObjectMapper mapper) {
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
