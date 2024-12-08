package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;

public abstract class Command {
    protected int timestamp;

    public abstract ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper);
}
