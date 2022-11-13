package ru.otus.Builders;

import ru.otus.*;

import java.util.ArrayList;
import java.util.Collections;

public class DispenserBuilder implements IDispenserBuilder {
    private Configuration configuration;

    private DispenserBuilder() {
    }
    public DispenserBuilder(Configuration configuration) {
        this.configuration = configuration;
    }
    public IDispenser Build(){
        var cashBoxes = new ArrayList<CashBox>();
        var  orderedValues= configuration.banknoteValues();
        Collections.sort(orderedValues, Collections.reverseOrder());

        for (var banknoteValue: orderedValues) {
            cashBoxes.add(new CashBox(banknoteValue));
        }
        return new Dispenser(cashBoxes);
    }
}
