package org.poo.commands;

import org.poo.fileio.CommandInput;

public class CommandFactory {
    private CommandFactory() { }

    public static Command create(CommandInput input) {
        return switch (input.getCommand()) {
            case PrintUsers.COMMAND -> new PrintUsers(input);
            case AddAccount.COMMAND -> new AddAccount(input);
            case AddFunds.COMMAND -> new AddFunds(input);
            case CreateCard.COMMAND -> new CreateCard(input, false);
            case CreateCard.COMMAND_ONE_TIME -> new CreateCard(input,true);
            case DeleteAccount.COMMAND -> new DeleteAccount(input);
            case DeleteCard.COMMAND -> new DeleteCard(input);
            case SetMinBalance.COMMAND -> new SetMinBalance(input);
            case PayOnline.COMMAND -> new PayOnline(input);
            case SendMoney.COMMAND -> new SendMoney(input);
            case PrintTransactions.COMMAND -> new PrintTransactions(input);
            case SetAlias.COMMAND -> new SetAlias(input);
            case CheckCardStatus.COMMAND -> new CheckCardStatus(input);
            case ChangeInterestRate.COMMAND -> new ChangeInterestRate(input);
            case SplitPayment.COMMAND -> new SplitPayment(input);

            default -> throw new IllegalArgumentException("Command not implemented: " + input.getCommand());
        };
    }
}
