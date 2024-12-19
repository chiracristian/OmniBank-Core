package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.exceptions.NonExistingIbanException;
import org.poo.exceptions.NotEnoughFundsException;
import org.poo.fileio.CommandInput;

final class SendMoney extends Command {
    public static final String COMMAND = "sendMoney";

    private final String account;
    private final double amount;
    private final String receiver;
    private final String email;
    private final String description;

    SendMoney(final CommandInput input) {
        super(input);
        this.account = input.getAccount();
        this.amount = input.getAmount();
        this.receiver = input.getReceiver();
        this.email = input.getEmail();
        this.description = input.getDescription();
    }

    @Override
    public ObjectNode executeAndGetOutput(final Bank bank, final ObjectMapper mapper) {
        try {
            bank.sendMoney(account, amount, receiver, timestamp, email, description);
        } catch (NotEnoughFundsException | NonExistingIbanException ignored) {

        }

        return null;
    }
}
