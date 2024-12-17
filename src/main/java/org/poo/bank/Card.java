package org.poo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import org.poo.bank.accounts.Account;
import org.poo.utils.Utils;

@Getter
public class Card {
    @Getter
    public enum Status {
        ACTIVE("active"),
        WARNING("warning"),
        FROZEN("frozen");

        private final String string;
        public static final double WARNING_LIMIT = 30.0;

        Status(String string) {
            this.string = string;
        }
    }
    private final String number;
    private final Account associatedAccount;
    private final boolean oneTimeUse;

    public Card(Account associatedAccount, boolean oneTimeUse) {
        this.number = Utils.generateCardNumber();
        this.associatedAccount = associatedAccount;

        this.oneTimeUse = oneTimeUse;
    }

    public Status getStatus() {
        double currentBalance = associatedAccount.getBalance();
        double minimumBalance = associatedAccount.getMinimumBalance();

        if (currentBalance <= minimumBalance) {
            return Status.FROZEN;
        }
        if (currentBalance - minimumBalance <= Status.WARNING_LIMIT) {
            return Status.WARNING;
        }

        return Status.ACTIVE;
    }

    public ObjectNode toJSON(ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("cardNumber", number);
        result.put("status", "active");

        return result;
    }
}
