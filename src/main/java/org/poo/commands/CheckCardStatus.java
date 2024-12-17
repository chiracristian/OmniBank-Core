package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.exceptions.NonExistingCardException;
import org.poo.fileio.CommandInput;

class CheckCardStatus extends Command {
    public static final String COMMAND = "checkCardStatus";

    private final String cardNumber;

    CheckCardStatus(CommandInput input) {
        super(input);
        this.cardNumber = input.getCardNumber();
    }

    @Override
    public ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper) {
        try {
            bank.checkCardStatus(cardNumber, timestamp);
        } catch (NonExistingCardException nonExistingCardException) {
            ObjectNode result = mapper.createObjectNode();

            result.put("command", COMMAND);

            ObjectNode outputNode = mapper.createObjectNode();
            outputNode.put("timestamp", timestamp);
            outputNode.put("description", "Card not found");

            result.set("output", outputNode);

            result.put("timestamp", timestamp);

            return result;
        }
        return null;
    }
}
