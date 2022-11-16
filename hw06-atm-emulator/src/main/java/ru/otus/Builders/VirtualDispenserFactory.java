package ru.otus.Builders;

import ru.otus.*;

import java.util.ArrayList;
import java.util.Collections;

public class VirtualDispenserFactory implements DispenserFactory {
    private Configuration configuration;

    private VirtualDispenserFactory() {
    }
    public VirtualDispenserFactory(Configuration configuration) {
        this.configuration = configuration;
    }
    public Dispenser build(){
        var cashBoxes = new ArrayList<CashBox>();
        var  orderedValues= configuration.banknoteValues();
        Collections.sort(orderedValues, Collections.reverseOrder());

        for (var banknoteValue: orderedValues) {
            cashBoxes.add(new CashBox(banknoteValue));
        }
        return new VirtualDispenser(cashBoxes);
    }
}
