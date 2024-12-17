package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

public abstract class Command {
    protected int timestamp;

    public Command(CommandInput input) {
        this.timestamp = input.getTimestamp();
    }

    public abstract ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper);
}
