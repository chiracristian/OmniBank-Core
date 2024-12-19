package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

final class PrintTransactions extends Command {
    public static final String COMMAND = "printTransactions";

    private final String email;

    PrintTransactions(final CommandInput input) {
        super(input);
        this.email = input.getEmail();
    }

    @Override
    public ObjectNode executeAndGetOutput(final Bank bank, final ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("command", COMMAND);

        ArrayNode outputNode = bank.getUserByEmail(email).getTransactionsAsJSON(mapper);
        result.set("output", outputNode);

        result.put("timestamp", timestamp);

        return result;
    }
}
