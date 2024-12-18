package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

class SetMinBalance extends Command {
    public static final String COMMAND = "setMinimumBalance";
    private final double amount;
    private final String account;

    SetMinBalance(CommandInput input) {
        super(input);
        this.amount = input.getAmount();
        this.account = input.getAccount();
    }

    @Override
    public ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper) {
        bank.getAccountByIban(account).setMinimumBalance(amount);
        return null;
    }
}
