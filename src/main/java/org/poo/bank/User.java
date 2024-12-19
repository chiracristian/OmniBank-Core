package org.poo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.poo.bank.accounts.Account;
import org.poo.transactions.Transaction;
import org.poo.fileio.UserInput;

import java.util.ArrayList;

@Data
public final class User {
    private String firstName;
    private String lastName;
    private String email;
    private final ArrayList<Account> accounts;
    private final ArrayList<Transaction> transactions;

    /**
     * Construct a new user
     * @param input the input used to create the user
     */
    public User(final UserInput input) {
        firstName = input.getFirstName();
        lastName = input.getLastName();
        email = input.getEmail();
        accounts = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    /**
     * Add an account to the user
     * @param account the account to add
     */
    public void addAccount(final Account account) {
        accounts.add(account);
    }

    /**
     * Add a transaction to the user
     * @param transaction the transaction to add
     */
    public void addTransaction(final Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * Get all the transactions the user made, in JSON format
     * @param mapper the ObjectMapper to use
     * @return the transactions, as an ArrayNode
     */
    public ArrayNode getTransactionsAsJSON(final ObjectMapper mapper) {
        ArrayNode result = mapper.createArrayNode();
        for (Transaction transaction : transactions) {
            result.add(transaction.toJson(mapper));
        }
        return result;
    }

    /**
     * Get the data of the user in JSON format
     * @param mapper the ObjectMapper to use
     * @return the data of the user, as an ObjectNode
     */
    public ObjectNode toJSON(final ObjectMapper mapper) {
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
