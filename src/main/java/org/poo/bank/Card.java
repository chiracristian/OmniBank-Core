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

        Status(String string) {
            this.string = string;
        }
    }
    private final String number;
    private final Account associatedAccount;
    private Status status;
    private final boolean oneTimeUse;

    public Card(Account associatedAccount, boolean oneTimeUse) {
        this.number = Utils.generateCardNumber();
        this.associatedAccount = associatedAccount;

        this.status = Status.ACTIVE;
        this.oneTimeUse = oneTimeUse;
    }

    public ObjectNode toJSON(ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("cardNumber", number);
        result.put("status", status.getString());

        return result;
    }
}
