package org.poo.commands;

import org.poo.fileio.CommandInput;

public class CommandFactory {
    private CommandFactory() { }

    public static Command create(CommandInput input) {
        return switch (input.getCommand()) {
            case "printUsers" -> new PrintUsers(input.getTimestamp());

            case "addAccount" -> new AddAccount(input.getEmail(), input.getCurrency(),
                    input.getAccountType(), input.getTimestamp(), input.getInterestRate());

            case "addFunds" -> new AddFunds(input.getAccount(), input.getAmount(), input.getTimestamp());

            case "createCard" -> new CreateCard(input.getAccount(),input.getEmail(), input.getTimestamp(), false);

            case "createOneTimeCard" -> new CreateCard(input.getAccount(),input.getEmail(), input.getTimestamp(), true);

            case "deleteAccount" -> new DeleteAccount(input.getAccount(), input.getTimestamp(), input.getEmail());

            default -> throw new IllegalArgumentException("Command not implemented: " + input.getCommand());
        };
    }
}
