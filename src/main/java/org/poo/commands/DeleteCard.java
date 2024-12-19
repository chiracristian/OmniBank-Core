package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.exceptions.NonExistingCardException;
import org.poo.fileio.CommandInput;

class DeleteCard extends Command {
    public static final String COMMAND = "deleteCard";

    private final String email;
    private final String cardNumber;

    DeleteCard(CommandInput input) {
        super(input);
        this.email = input.getEmail();
        this.cardNumber = input.getCardNumber();
        this.timestamp = input.getTimestamp();
    }

    @Override
    public ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper) {
        try {
            bank.deleteCard(email, cardNumber, timestamp);
        } catch (NonExistingCardException ignored) {

        }

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
