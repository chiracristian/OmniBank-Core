package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public final class CommandInvoker {
    private final ArrayList<Command> commands;
    private final Bank refBank;
    private final ObjectMapper refMapper;

    /**
     * Construct a command invoker
     * @param input the entered commands
     * @param refBank the referenced bank
     * @param refMapper the objectMapper used for JSON output
     */
    public CommandInvoker(final CommandInput[] input, final Bank refBank,
                          final ObjectMapper refMapper) {
        commands = new ArrayList<>(input.length);
        for (CommandInput cmdIn : input) {
            try {
                commands.add(CommandFactory.create(cmdIn));
            } catch (IllegalArgumentException argumentException) {
                System.out.println(argumentException.getMessage());
                break;
            }
        }
        this.refBank = refBank;
        this.refMapper = refMapper;
    }

    /**
     * Execute all the commands
     * @return an ArrayNode with the output of the commands
     */
    public ArrayNode executeAll() {
        ArrayNode result = refMapper.createArrayNode();

        for (Command command : commands) {
            ObjectNode output;
            try {
                output = command.executeAndGetOutput(refBank, refMapper);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                break;
            }

            if (output != null) {
                result.add(output);
            }
        }

        return result;
    }
}
