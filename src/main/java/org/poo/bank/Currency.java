package org.poo.bank;

import lombok.Getter;

@Getter
public enum Currency {
    RON("RON"),
    EUR("EUR"),
    USD("USD");

    private final String name;

    Currency(String name) {
        this.name = name;
    }

    public static Currency fromString(String name) {
        for (Currency currency : Currency.values()) {
            if (currency.name.equals(name)) {
                return currency;
            }
        }
        throw new IllegalArgumentException("Currency " + name + " isn't supported");
    }
}
