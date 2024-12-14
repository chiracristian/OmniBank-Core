package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.accounts.NotEnoughFundsException;

public class SendMoney extends Command {
    public static final String COMMAND = "sendMoney";

    private final String account;
    private final double amount;
    private final String receiver;
    private final String description;

    public SendMoney(String account, double amount, String receiver, int timestamp,
                     String description) {
        this.account = account;
        this.amount = amount;
        this.receiver = receiver;
        this.timestamp = timestamp;
        this.description = description;
    }

    @Override
    public ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper) {
        ObjectNode result = null;
        try {
            bank.sendMoney(account, amount, receiver, description);
        } catch (NotEnoughFundsException ignored) {

        }

        return result;
    }
}
