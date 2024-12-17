package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;

class PrintTransactions extends Command {
    public static final String COMMAND = "printTransactions";

    private final String email;

    PrintTransactions(int timestamp, String email) {
        this.timestamp = timestamp;
        this.email = email;
    }

    @Override
    public ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("command", COMMAND);

        ArrayNode outputNode = bank.getUsers().get(email).getTransactionsAsJSON(mapper);
        result.set("output", outputNode);

        result.put("timestamp", timestamp);

        return result;
    }
}
