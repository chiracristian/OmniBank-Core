package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;
import org.poo.bank.exceptions.NonExistingIbanException;
import org.poo.fileio.CommandInput;

class SpendingsReport extends Command {
    public static final String COMMAND = "spendingsReport";
    private final int startTimestamp;
    private final int endTimestamp;
    private final String account;

    public SpendingsReport(CommandInput input) {
        super(input);
        this.startTimestamp = input.getStartTimestamp();
        this.endTimestamp = input.getEndTimestamp();
        this.account = input.getAccount();
    }

    @Override
    public ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("command", COMMAND);

        ObjectNode outputNode;
        Account refAccount;
        try {
            refAccount = bank.getAccountByIban(account);
        }  catch (NonExistingIbanException nonExistingIbanException) {
            outputNode = mapper.createObjectNode();
            outputNode.put("timestamp", timestamp);
            outputNode.put("description", "Account not found");

            result.set("output", outputNode);
            result.put("timestamp", timestamp);

            return result;
        }

        outputNode = refAccount.getSpendingReport(mapper, startTimestamp, endTimestamp);
        result.set("output", outputNode);

        result.put("timestamp", timestamp);

        return result;
    }
}
