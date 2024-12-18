package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

class ChangeInterestRate extends Command {
    public static final String COMMAND = "changeInterestRate";
    private final String account;
    private final double interestRate;

    ChangeInterestRate(CommandInput input) {
        super(input);
        this.account = input.getAccount();
        this.interestRate = input.getInterestRate();
    }

    @Override
    public ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper) {
        bank.getAccountByIban(account).changeInterestRate(interestRate, timestamp);

        return null;
    }
}
