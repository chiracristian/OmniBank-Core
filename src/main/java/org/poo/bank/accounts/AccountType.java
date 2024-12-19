package org.poo.bank.accounts;

import lombok.Getter;

@Getter
public enum AccountType {
    CLASSIC("classic"),
    SAVINGS("savings");

    private final String string;

    AccountType(final String string) {
        this.string = string;
    }

    /**
     * @param string the name of the account type as string
     * @return the corresponding enum instance
     */
    public static AccountType fromString(final String string) {
        for (AccountType type : AccountType.values()) {
            if (type.string.equals(string)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Accounts of type " + string + " aren't supported");
    }
}
