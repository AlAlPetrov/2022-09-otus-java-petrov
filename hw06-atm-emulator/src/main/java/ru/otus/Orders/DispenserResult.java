package ru.otus.Orders;

import ru.otus.BanknoteBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class DispenserResult {
    protected final UUID id;
    protected int requestedAmount;
    private List<BanknoteBatch> banknoteBatches = new ArrayList<BanknoteBatch>();
    public DispenserResult(int amount) {
        this.requestedAmount = amount;
        id = java.util.UUID.randomUUID();
    }
    public int getRequiredAmount() {
        return requestedAmount- banknoteBatches.stream()
                .mapToInt(o -> o.total())
                .sum();
    }

    public List<BanknoteBatch> getBanknoteBatches() {
        return  Collections.unmodifiableList(banknoteBatches);
    }
    public void addBatch(BanknoteBatch banknoteBatch) {
        banknoteBatches.add(banknoteBatch);
    }

    public boolean isCompleted() {
        return getRequiredAmount() == 0;
    }

    @Override public boolean equals(Object other) {
        if (!(other instanceof DispenserResult)) {
            return false;
        }
        return id == ((DispenserResult)other).id;
    }

    @Override public String toString() {
        return id.toString();
    }
}
