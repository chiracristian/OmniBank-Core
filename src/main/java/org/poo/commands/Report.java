package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;
import org.poo.bank.accounts.transactions.Transaction;
import org.poo.fileio.CommandInput;

class Report extends Command {
    public static final String COMMAND = "report";
    private final int startTimestamp;
    private final int endTimestamp;
    private final String account;

    public Report(CommandInput input) {
        super(input);
        this.startTimestamp = input.getStartTimestamp();
        this.endTimestamp = input.getEndTimestamp();
        this.account = input.getAccount();
    }

    @Override
    public ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("command", COMMAND);

        Account refAccount = bank.getAccountByIban(account);

        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("IBAN", account);
        outputNode.put("balance", refAccount.getBalance());
        outputNode.put("currency", refAccount.getCurrency());

        ArrayNode transactionsNode = mapper.createArrayNode();

        for (Transaction transaction : refAccount.getTransactions()) {
            if (transaction.getTimestamp() < startTimestamp) {
                continue;
            } else if (transaction.getTimestamp() > endTimestamp) {
                break;
            }
            transactionsNode.add(transaction.toJson(mapper));
        }
        outputNode.set("transactions", transactionsNode);
        result.set("output", outputNode);

        result.put("timestamp", timestamp);

        return result;
    }
}
