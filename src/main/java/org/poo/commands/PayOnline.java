package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.NonExistingCardException;
import org.poo.bank.accounts.NotEnoughFundsException;

public class PayOnline extends Command {
    public static final String COMMAND = "payOnline";

    private final String cardNumber;
    private final double amount;
    private final String currency;
    private final String description;
    private final String commerciant;
    private final String email;

    public PayOnline(String cardNumber, double amount, String currency, int timestamp,
                     String commerciant, String description, String email) {
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = timestamp;
        this.commerciant = commerciant;
        this.description = description;
        this.email = email;
    }

    @Override
    public ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper) {
        ObjectNode result = null;
        try {
            bank.payOnline(cardNumber, amount, currency, description, commerciant, email);
        } catch (NonExistingCardException nonExistingCardException) {
            result = mapper.createObjectNode();
            result.put("command", COMMAND);

            ObjectNode outputNode = mapper.createObjectNode();
            outputNode.put("timestamp", timestamp);
            outputNode.put("description", "Card not found");

            result.set("output", outputNode);

            result.put("timestamp", timestamp);
        } catch (NotEnoughFundsException ignored) {

        }

        return result;
    }
}
