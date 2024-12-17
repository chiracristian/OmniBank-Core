package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;

class DeleteCard extends Command {
    public static final String COMMAND = "deleteCard";

    private final String email;
    private final String cardNumber;

    DeleteCard(String email, String cardNumber, int timestamp) {
        this.email = email;
        this.cardNumber = cardNumber;
        this.timestamp = timestamp;
    }

    @Override
    public ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper) {
        bank.deleteCard(email, cardNumber, timestamp);

//        ObjectNode result = mapper.createObjectNode();
//        result.put("command", COMMAND);
//
//        ObjectNode outputNode = mapper.createObjectNode();
//        outputNode.put("success", "Account deleted");
//        outputNode.put("timestamp", timestamp);
//
//        result.set("output", outputNode);
//
//        result.put("timestamp", timestamp);

        return null;
    }
}
