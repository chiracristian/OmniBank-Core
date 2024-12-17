package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

public class SetAlias extends Command {
    public static final String COMMAND = "setAlias";

    private final String email;
    private final String account;
    private final String alias;

    public SetAlias(CommandInput input) {
        super(input);
        this.email = input.getEmail();
        this.account = input.getAccount();
        this.alias = input.getAlias();
    }

    @Override
    public ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper) {
        bank.setAlias(email, account, alias);
        return null;
    }
}
