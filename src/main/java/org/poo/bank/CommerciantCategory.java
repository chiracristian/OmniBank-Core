package org.poo.bank;

import lombok.Getter;
import org.poo.fileio.CommerciantInput;

import java.util.ArrayList;

@Getter
public class CommerciantCategory {
    private final int id;
    private final String description;
    private final ArrayList<String> commerciants;

    public CommerciantCategory(CommerciantInput input) {
        this.id = input.getId();
        this.description = input.getDescription();
        this.commerciants = new ArrayList<>();
        commerciants.addAll(input.getCommerciants());
    }
}
