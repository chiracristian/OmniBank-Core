package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.exceptions.NonExistingIbanException;
import org.poo.bank.exceptions.NotEnoughFundsException;
import org.poo.fileio.CommandInput;

import java.util.List;

public class SplitPayment extends Command {
    public static final String COMMAND = "splitPayment";

    private final List<String> accountsForSplit;
    private final String currency;
    private final double amount;

    public SplitPayment(CommandInput input) {
        super(input);
        this.accountsForSplit = input.getAccounts();
        this.currency = input.getCurrency();
        this.amount = input.getAmount();
    }

    @Override
    public ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper) {
        try {
            bank.splitPayment(accountsForSplit, timestamp, currency, amount);
        } catch (NotEnoughFundsException | NonExistingIbanException ignored) {

        }

        return null;
    }
}
