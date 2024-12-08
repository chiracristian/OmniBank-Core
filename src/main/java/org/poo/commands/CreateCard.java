package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;

public class CreateCard extends Command {
    private final String command;
    private final String account;
    private final String email;
    private final boolean oneTimeUse;

    public CreateCard(String account, String email, int timestamp, boolean oneTimeUse) {
        if (oneTimeUse) {
            this.command = "createOneTimeCard";
        } else {
            this.command = "createCard";
        }

        this.account = account;
        this.email = email;
        this.timestamp = timestamp;
        this.oneTimeUse = oneTimeUse;
    }

    @Override
    public ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper) {
        //bank.getAccounts().get(account).createCard(oneTimeUse);
        bank.createCard(account, email, oneTimeUse);
        return null;
    }
}
