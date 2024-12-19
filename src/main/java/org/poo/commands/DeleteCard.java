package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.exceptions.NonExistingCardException;
import org.poo.fileio.CommandInput;

final class DeleteCard extends Command {
    public static final String COMMAND = "deleteCard";

    private final String email;
    private final String cardNumber;

    DeleteCard(final CommandInput input) {
        super(input);
        this.email = input.getEmail();
        this.cardNumber = input.getCardNumber();
    }

    @Override
    public ObjectNode executeAndGetOutput(final Bank bank, final ObjectMapper mapper) {
        try {
            bank.deleteCard(email, cardNumber, timestamp);
        } catch (NonExistingCardException ignored) {

        }

        return null;
    }
}
