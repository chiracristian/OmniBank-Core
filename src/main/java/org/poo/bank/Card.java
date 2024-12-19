package org.poo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.accounts.Account;
import org.poo.utils.Utils;

@Getter
public final class Card {
    @Getter
    public enum Status {
        ACTIVE("active"),
        WARNING("warning"),
        FROZEN("frozen");

        private final String string;
        public static final double WARNING_LIMIT = 30.0;

        Status(final String string) {
            this.string = string;
        }
    }
    private final String number;
    private final Account associatedAccount;
    private final boolean oneTimeUse;

    @Setter
    private Status status;

    /**
     * Construct a new card
     * @param associatedAccount the account the card shall belong to
     * @param oneTimeUse whether it is one time use or not
     */
    public Card(final Account associatedAccount, final boolean oneTimeUse) {
        this.number = Utils.generateCardNumber();
        this.associatedAccount = associatedAccount;
        this.oneTimeUse = oneTimeUse;

        this.status = Status.ACTIVE;
    }

    /**
     * Update the status of the card
     */
    public void updateStatus() {
        double currentBalance = associatedAccount.getBalance();
        double minimumBalance = associatedAccount.getMinimumBalance();

        if (currentBalance <= minimumBalance) {
            status = Status.FROZEN;
        } else if (currentBalance - minimumBalance <= Status.WARNING_LIMIT) {
            status = Status.WARNING;
        } else if (status == Status.WARNING) {
            status = Status.ACTIVE;
        }
    }

    /**
     * Get the data of the Card in JSON format
     * @param mapper the ObjectMapper to use
     * @return the data of the card, as an ObjectNode
     */
    public ObjectNode toJSON(final ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("cardNumber", number);
        result.put("status", status.getString());

        return result;
    }
}
