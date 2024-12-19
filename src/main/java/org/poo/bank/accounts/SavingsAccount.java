package org.poo.bank.accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import org.poo.bank.accounts.transactions.Transaction;

@Getter
public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String ownerEmail, String currency, double interestRate) {
        super(ownerEmail, currency);
        this.interestRate = interestRate;
    }

    @Override
    public String getType() {
        return AccountType.SAVINGS.getString();
    }

    @Override
    public ObjectNode getReport(ObjectMapper mapper, int startTimestamp, int endTimestamp) {
        ObjectNode outputNode = mapper.createObjectNode();

        outputNode.put("IBAN", getIban());
        outputNode.put("balance", getBalance());
        outputNode.put("currency", getCurrency());

        ArrayNode transactionsNode = mapper.createArrayNode();
        for (Transaction transaction : getTransactions()) {
            if (transaction.getTimestamp() < startTimestamp) {
                continue;
            } else if (transaction.getTimestamp() > endTimestamp) {
                break;
            }
            if (transaction.displayedInSavingAccountsReports()) {
                transactionsNode.add(transaction.toJson(mapper));
            }
        }
        outputNode.set("transactions", transactionsNode);

        return outputNode;
    }

    @Override
    public ObjectNode getSpendingReport(ObjectMapper mapper, int startTimestamp, int endTimestamp) {
        ObjectNode result = mapper.createObjectNode();
        result.put("error", "This kind of report is not supported for a saving account");
        return result;
    }

    @Override
    public double addInterest() {
        double addedAmount = getBalance() * interestRate;
        addFunds(addedAmount);
        return addedAmount;
    }

    @Override
    public void changeInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
}
