package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.exceptions.NotSavingsAccountException;
import org.poo.fileio.CommandInput;

final class AddInterest extends Command {
    public static final String COMMAND = "addInterest";

    private final String account;

    AddInterest(final CommandInput input) {
        super(input);
        this.account = input.getAccount();
    }

    @Override
    public ObjectNode executeAndGetOutput(final Bank bank, final ObjectMapper mapper) {
        try {
            bank.addInterest(account, timestamp);
        } catch (NotSavingsAccountException notSavingsAccountException) {
            ObjectNode result = mapper.createObjectNode();

            result.put("command", COMMAND);

            ObjectNode outputNode = mapper.createObjectNode();
            outputNode.put("timestamp", timestamp);
            outputNode.put("description", "This is not a savings account");

            result.set("output", outputNode);
            result.put("timestamp", timestamp);

            return result;
        }

        return null;
    }
}
