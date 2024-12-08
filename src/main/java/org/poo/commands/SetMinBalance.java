package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;

class SetMinBalance extends Command {
    private static final String COMMAND = "setMinimumBalance";
    private final double amount;
    private final String account;

    SetMinBalance(double amount, String account, int timestamp) {
        this.amount = amount;
        this.account = account;
        this.timestamp = timestamp;
    }

    @Override
    public ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper) {
        bank.getAccounts().get(account).setMinimumBalance(amount);
        return null;
    }
}
