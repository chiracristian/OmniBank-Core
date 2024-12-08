package org.poo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.poo.fileio.UserInput;

import java.util.ArrayList;

@Data
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private final ArrayList<Account> accounts;

    User(UserInput input) {
        firstName = input.getFirstName();
        lastName = input.getLastName();
        email = input.getEmail();
        accounts = new ArrayList<>();
    }

    public Account addAccount(Currency currency, AccountType type, double interestRate) {
        switch (type) {
            case AccountType.CLASSIC -> accounts.add(new Account(currency));
            case AccountType.SAVINGS -> accounts.add(new SavingsAccount(currency, interestRate));
        }

        return accounts.getLast();
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
