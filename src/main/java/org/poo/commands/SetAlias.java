package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;

public class SetAlias extends Command {
    public static final String COMMAND = "setAlias";

    private final String email;
    private final String account;
    private final String alias;

    public SetAlias(String email, String account, String alias, int timestamp) {
        this.email = email;
        this.account = account;
        this.alias = alias;
        this.timestamp = timestamp;
    }

    @Override
    public ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper) {
        bank.setAlias(email, account, alias);
        return null;
    }
}
