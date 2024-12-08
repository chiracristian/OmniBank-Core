package org.poo.bank;

import lombok.Getter;

@Getter
public enum CardStatus {
    ACTIVE("active"),
    WARNING("warning"),
    FROZEN("frozen");

    private final String string;

    CardStatus(String string) {
        this.string = string;
    }
}
