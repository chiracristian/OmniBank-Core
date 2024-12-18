package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.accounts.transactions.CardCreated;
import org.poo.bank.Card;
import org.poo.fileio.CommandInput;

public class CreateCard extends Command {
    public static final String COMMAND = "createCard";
    public static final String COMMAND_ONE_TIME = "createOneTimeCard";

//    private final String command;
    private final String account;
    private final String email;
    private final boolean oneTimeUse;

    public CreateCard(CommandInput input, boolean oneTimeUse) {
        super(input);
//        if (oneTimeUse) {
//            this.command = CreateCard.COMMAND_ONE_TIME;
//        } else {
//            this.command = CreateCard.COMMAND;
//        }

        this.account = input.getAccount();
        this.email = input.getEmail();
        this.oneTimeUse = oneTimeUse;
    }

    @Override
    public ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper) {
        Card createdCard = bank.createCard(account, email, oneTimeUse);
        bank.getUserByEmail(email).addTransaction(new CardCreated(timestamp, createdCard.getNumber(), email, account));
        return null;
    }
}
