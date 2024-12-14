package org.poo.bank.accounts;

import lombok.Getter;

@Getter
public enum AccountType {
    CLASSIC("classic"),
    SAVINGS("savings");

    private final String string;

    AccountType(String string) {
        this.string = string;
    }

    public static AccountType fromString(String string) {
        for (AccountType type : AccountType.values()) {
            if (type.string.equals(string)) {
                return type;
            }
        }
        throw new IllegalArgumentException(string + " account isn't supported");
    }
}
