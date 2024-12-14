package org.poo.commands;

import org.poo.fileio.CommandInput;

public class CommandFactory {
    private CommandFactory() { }

    public static Command create(CommandInput input) {
        return switch (input.getCommand()) {
            case PrintUsers.COMMAND -> new PrintUsers(input.getTimestamp());

            case AddAccount.COMMAND -> new AddAccount(input.getEmail(), input.getCurrency(),
                    input.getAccountType(), input.getTimestamp(), input.getInterestRate());

            case AddFunds.COMMAND -> new AddFunds(input.getAccount(), input.getAmount(), input.getTimestamp());

            case CreateCard.COMMAND -> new CreateCard(input.getAccount(), input.getEmail(),
                    input.getTimestamp(),false);

            case CreateCard.COMMAND_ONE_TIME -> new CreateCard(input.getAccount(), input.getEmail(),
                    input.getTimestamp(), true);

            case DeleteAccount.COMMAND -> new DeleteAccount(input.getAccount(), input.getTimestamp(),
                    input.getEmail());

            case DeleteCard.COMMAND -> new DeleteCard(input.getCardNumber(), input.getTimestamp());

            case SetMinBalance.COMMAND -> new SetMinBalance(input.getAmount(), input.getAccount(),
                    input.getTimestamp());

            case PayOnline.COMMAND -> new PayOnline(input.getCardNumber(), input.getAmount(), input.getCurrency(),
                    input.getTimestamp(), input.getCommerciant(), input.getDescription(), input.getEmail());

            default -> throw new IllegalArgumentException("Command not implemented: " + input.getCommand());
        };
    }
}
