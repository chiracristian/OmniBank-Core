package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.User;

class PrintUsers extends Command {
    private static final String COMMAND = "printUsers";

    PrintUsers(int timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("command", COMMAND);

        ArrayNode outputNode = mapper.createArrayNode();
        for (User user : bank.getUsers().values()) {
            outputNode.add(user.toJSON(mapper));
        }
        result.set("output", outputNode);

        result.put("timestamp", timestamp);

        return result;
    }
}
