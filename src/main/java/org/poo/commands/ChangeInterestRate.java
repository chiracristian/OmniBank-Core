package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.exceptions.NotSavingsAccountException;
import org.poo.fileio.CommandInput;

final class ChangeInterestRate extends Command {
    public static final String COMMAND = "changeInterestRate";
    private final String account;
    private final double interestRate;

    ChangeInterestRate(final CommandInput input) {
        super(input);
        this.account = input.getAccount();
        this.interestRate = input.getInterestRate();
    }

    @Override
    public ObjectNode executeAndGetOutput(final Bank bank, final ObjectMapper mapper) {
        try {
            bank.changeInterestRate(account, interestRate, timestamp);
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
