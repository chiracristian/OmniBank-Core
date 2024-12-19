package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.exceptions.NonExistingCardException;
import org.poo.exceptions.NonExistingIbanException;
import org.poo.exceptions.NotEnoughFundsException;
import org.poo.fileio.CommandInput;

final class PayOnline extends Command {
    public static final String COMMAND = "payOnline";

    private final String cardNumber;
    private final double amount;
    private final String currency;
    private final String description;
    private final String commerciant;
    private final String email;

    PayOnline(final CommandInput input) {
        super(input);
        this.cardNumber = input.getCardNumber();
        this.amount = input.getAmount();
        this.currency = input.getCurrency();
        this.description = input.getDescription();
        this.commerciant = input.getCommerciant();
        this.email = input.getEmail();
    }

    @Override
    public ObjectNode executeAndGetOutput(final Bank bank, final ObjectMapper mapper) {
        try {
            bank.payOnline(cardNumber, amount, currency, timestamp, commerciant, email);
        } catch (NonExistingCardException nonExistingCardException) {
            ObjectNode result = mapper.createObjectNode();
            result.put("command", COMMAND);

            ObjectNode outputNode = mapper.createObjectNode();
            outputNode.put("timestamp", timestamp);
            outputNode.put("description", "Card not found");

            result.set("output", outputNode);

            result.put("timestamp", timestamp);
            return result;
        } catch (NotEnoughFundsException | NonExistingIbanException ignored) {

        }
        return null;
    }
}
