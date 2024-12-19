package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

class DeleteAccount extends Command {
    public static final String COMMAND = "deleteAccount";

    private final String account;
    private final String email;

    DeleteAccount(CommandInput input) {
        super(input);
        this.account = input.getAccount();
        this.timestamp = input.getTimestamp();
        this.email = input.getEmail();
    }

    @Override
    public ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper) {


        ObjectNode result = mapper.createObjectNode();
        result.put("command", COMMAND);

        ObjectNode outputNode = mapper.createObjectNode();

        try {
            bank.deleteAccount(account, email, timestamp);
            outputNode.put("success", "Account deleted");
        } catch (Exception e) {
            outputNode.put("error", "Account couldn't be deleted - see org.poo.transactions for details");
        }

        outputNode.put("timestamp", timestamp);

        result.set("output", outputNode);

        result.put("timestamp", timestamp);

        return result;
    }
}
