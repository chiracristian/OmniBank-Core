package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

final class SetAlias extends Command {
    public static final String COMMAND = "setAlias";

    private final String email;
    private final String account;
    private final String alias;

    SetAlias(final CommandInput input) {
        super(input);
        this.email = input.getEmail();
        this.account = input.getAccount();
        this.alias = input.getAlias();
    }

    @Override
    public ObjectNode executeAndGetOutput(final Bank bank, final ObjectMapper mapper) {
        bank.setAlias(email, account, alias);
        return null;
    }
}
