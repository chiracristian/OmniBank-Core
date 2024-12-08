package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public class CommandManager {
    private final ArrayList<Command> commands;
    private final Bank refBank;
    private final ObjectMapper refMapper;

    public CommandManager(CommandInput[] input, Bank refBank, ObjectMapper refMapper) {
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

    public ArrayNode executeAll() {
        ArrayNode result = refMapper.createArrayNode();

        for (Command command : commands) {
            ObjectNode output;
            try {
                output = command.executeAndGetOutput(refBank, refMapper);
            } catch (Exception e) {
                System.out.println(e.getMessage() + "\n");
                break;
            }

            if (output != null) {
                result.add(output);
            }
        }

        System.out.println("Ended executing commands for current test\n");

        return result;
    }
}
