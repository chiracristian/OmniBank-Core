package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

final class AddFunds extends Command {
    public static final String COMMAND = "addFunds";

    private final String account;
    private final double amount;

    AddFunds(final CommandInput input) {
        super(input);
        this.account = input.getAccount();
        this.amount = input.getAmount();
    }

    @Override
    public ObjectNode executeAndGetOutput(final Bank bank, final ObjectMapper mapper) {
        bank.getAccountByIban(account).addFunds(amount);
        return null;
    }
}
