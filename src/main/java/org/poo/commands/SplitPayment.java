package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.exceptions.NonExistingIbanException;
import org.poo.exceptions.NotEnoughFundsException;
import org.poo.fileio.CommandInput;

import java.util.List;

final class SplitPayment extends Command {
    public static final String COMMAND = "splitPayment";

    private final List<String> accountsForSplit;
    private final String currency;
    private final double amount;

    SplitPayment(final CommandInput input) {
        super(input);
        this.accountsForSplit = input.getAccounts();
        this.currency = input.getCurrency();
        this.amount = input.getAmount();
    }

    @Override
    public ObjectNode executeAndGetOutput(final Bank bank, final ObjectMapper mapper) {
        try {
            bank.splitPayment(accountsForSplit, timestamp, currency, amount);
        } catch (NotEnoughFundsException | NonExistingIbanException ignored) {

        }

        return null;
    }
}
