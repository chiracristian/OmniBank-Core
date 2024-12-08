package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;

class DeleteCard extends Command {
    private static final String COMMAND = "deleteCard";
    private final String account;

    DeleteCard(String account, int timestamp) {
        this.account = account;
        this.timestamp = timestamp;
    }

    @Override
    public ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper) {
        bank.deleteCard(account);

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
