package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

abstract class Command {
    protected final int timestamp;

    Command(final CommandInput input) {
        this.timestamp = input.getTimestamp();
    }

    public abstract ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper);
}
