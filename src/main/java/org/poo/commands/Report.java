package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;
import org.poo.exceptions.NonExistingIbanException;
import org.poo.fileio.CommandInput;

final class Report extends Command {
    public static final String COMMAND = "report";
    private final int startTimestamp;
    private final int endTimestamp;
    private final String account;

    Report(final CommandInput input) {
        super(input);
        this.startTimestamp = input.getStartTimestamp();
        this.endTimestamp = input.getEndTimestamp();
        this.account = input.getAccount();
    }

    @Override
    public ObjectNode executeAndGetOutput(final Bank bank, final ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("command", COMMAND);

        ObjectNode outputNode;
        Account refAccount;
        try {
            refAccount = bank.getAccountByIban(account);
        } catch (NonExistingIbanException nonExistingIbanException) {
            outputNode = mapper.createObjectNode();
            outputNode.put("timestamp", timestamp);
            outputNode.put("description", "Account not found");

            result.set("output", outputNode);
            result.put("timestamp", timestamp);

            return result;
        }

        outputNode = refAccount.getReport(mapper, startTimestamp, endTimestamp);
        result.set("output", outputNode);

        result.put("timestamp", timestamp);

        return result;
    }
}
