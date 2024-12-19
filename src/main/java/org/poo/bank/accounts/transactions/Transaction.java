package org.poo.bank.accounts.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;

@Getter
abstract public class Transaction {
    protected final int timestamp;

    public Transaction(int timestamp) {
        this.timestamp = timestamp;
    }

    public boolean displayedInSavingAccountsReports() {
        return false;
    }

    public boolean displayedInSpendingReports() {
        return false;
    }

    abstract public ObjectNode toJson(ObjectMapper mapper);
}
