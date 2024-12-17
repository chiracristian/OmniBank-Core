package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.exceptions.NonExistingIbanException;
import org.poo.bank.exceptions.NotEnoughFundsException;
import org.poo.fileio.CommandInput;

public class SendMoney extends Command {
    public static final String COMMAND = "sendMoney";

    private final String account;
    private final double amount;
    private final String receiver;
    private final String email;
    private final String description;

    public SendMoney(CommandInput input) {
        super(input);
        this.account = input.getAccount();
        this.amount = input.getAmount();
        this.receiver = input.getReceiver();
        this.email = input.getEmail();
        this.description = input.getDescription();
    }

    @Override
    public ObjectNode executeAndGetOutput(Bank bank, ObjectMapper mapper) {
        try {
            bank.sendMoney(account, amount, receiver, timestamp, email, description);
        } catch (NotEnoughFundsException | NonExistingIbanException ignored) {

        }

        return null;
    }
}
