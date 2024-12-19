package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

final class SetMinBalance extends Command {
    public static final String COMMAND = "setMinimumBalance";
    private final double amount;
    private final String account;

    SetMinBalance(final CommandInput input) {
        super(input);
        this.amount = input.getAmount();
        this.account = input.getAccount();
    }

    @Override
    public ObjectNode executeAndGetOutput(final Bank bank, final ObjectMapper mapper) {
        bank.getAccountByIban(account).setMinimumBalance(amount);
        return null;
    }
}
