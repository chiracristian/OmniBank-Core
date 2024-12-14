package org.poo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import org.poo.bank.accounts.Account;
import org.poo.utils.Utils;

@Getter
public class Card {
    private final String number;
    private final Account associatedAccount;

    private CardStatus status;
    private final boolean oneTimeUse;
    private boolean deleted;

    public Card(Account associatedAccount, boolean oneTimeUse) {
        this.number = Utils.generateCardNumber();
        this.associatedAccount = associatedAccount;

        this.status = CardStatus.ACTIVE;
        this.oneTimeUse = oneTimeUse;
        this.deleted = false;
    }

    void markAsDeleted() {
        deleted = true;
    }

    public ObjectNode toJSON(ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("cardNumber", number);
        result.put("status", status.getString());

        return result;
    }
}
