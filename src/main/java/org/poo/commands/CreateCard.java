package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.exceptions.AccountEmailMismatchException;
import org.poo.fileio.CommandInput;

final class CreateCard extends Command {
    public static final String COMMAND = "createCard";
    public static final String COMMAND_ONE_TIME = "createOneTimeCard";

    private final String account;
    private final String email;
    private final boolean oneTimeUse;

    CreateCard(final CommandInput input, final boolean oneTimeUse) {
        super(input);

        this.account = input.getAccount();
        this.email = input.getEmail();
        this.oneTimeUse = oneTimeUse;
    }

    @Override
    public ObjectNode executeAndGetOutput(final Bank bank, final ObjectMapper mapper) {
        try {
            bank.createCard(account, email, oneTimeUse, timestamp);
        } catch (AccountEmailMismatchException ignored) {

        }

        return null;
    }
}
