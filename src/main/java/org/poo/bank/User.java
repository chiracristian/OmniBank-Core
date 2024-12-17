package org.poo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.poo.bank.accounts.Account;
import org.poo.bank.accounts.transactions.Transaction;
import org.poo.fileio.UserInput;

import java.util.ArrayList;

@Data
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private final ArrayList<Account> accounts;
    private final ArrayList<Transaction> transactions;

    User(UserInput input) {
        firstName = input.getFirstName();
        lastName = input.getLastName();
        email = input.getEmail();
        accounts = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public ArrayNode getTransactionsAsJSON(ObjectMapper mapper) {
        ArrayNode result = mapper.createArrayNode();
        for (Transaction transaction : transactions) {
            result.add(transaction.toJson(mapper));
        }
        return result;
    }

    public ObjectNode toJSON(ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();

        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("email", email);

        ArrayNode accountsNode = mapper.createArrayNode();
        for (Account account : accounts) {
            accountsNode.add(account.toJSON(mapper));
        }
        result.set("accounts", accountsNode);

        return result;
    }
}
