package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;

@Getter
public abstract class Transaction {
    protected final int timestamp;

    public Transaction(final int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return whether the transaction is displayed in reports for savings accounts
     */
    public boolean displayedInSavingAccountsReports() {
        return false;
    }

    /**
     * @return whether the transaction is displayed in spending reports
     */
    public boolean displayedInSpendingReports() {
        return false;
    }

    /**
     * Get the data of the transaction in JSON format
     * @param mapper the ObjectMapper to use
     * @return the data of the transaction, as an ObjectNode
     */
    public abstract ObjectNode toJson(ObjectMapper mapper);
}
