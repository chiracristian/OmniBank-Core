package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

public class AddFunds extends Command {
    public static final String COMMAND = "addFunds";

    private final String account;
    private final double amount;

    public AddFunds(CommandInput input) {
        super(input);
        this.account = input.getAccount();
        this.amount = input.getAmount();
    }

    @Override
    public ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper) {
        bank.getAccounts().get(account).addFunds(amount);
        return null;
    }
}
