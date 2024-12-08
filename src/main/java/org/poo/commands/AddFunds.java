package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;

public class AddFunds extends Command {
    private static final String COMMAND = "addFunds";
    private final String account;
    private final double amount;

    public AddFunds(String account, double amount, int timestamp) {
        this.account = account;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    @Override
    public ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper) {
        bank.getAccounts().get(account).addFunds(amount);
        return null;
    }
}
