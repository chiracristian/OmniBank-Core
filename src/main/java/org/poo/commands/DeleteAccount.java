package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;

class DeleteAccount extends Command {
    private static final String COMMAND = "deleteAccount";
    private final String account;
    private final String email;

    DeleteAccount(String account, int timestamp, String email) {
        this.account = account;
        this.timestamp = timestamp;
        this.email = email;
    }

    @Override
    public ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper) {
        bank.deleteAccount(account, email);

        ObjectNode result = mapper.createObjectNode();
        result.put("command", COMMAND);

        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("success", "Account deleted");
        outputNode.put("timestamp", timestamp);

        result.set("output", outputNode);

        result.put("timestamp", timestamp);

        return result;
    }
}
