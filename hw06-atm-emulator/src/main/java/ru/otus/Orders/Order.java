package ru.otus.Orders;

import ru.otus.BanknoteBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Order {
    protected final UUID id;
    protected int requestedAmount;
    private List<BanknoteBatch> banknoteBatches = new ArrayList<BanknoteBatch>();
    public Order(int amount) {
        this.requestedAmount = amount;
        id = java.util.UUID.randomUUID();
    }
    public int GetRequiredAmount() {
        return requestedAmount- banknoteBatches.stream()
                .mapToInt(o -> o.total())
                .sum();
    }

    public List<BanknoteBatch> GetBanknoteBatches() {
        return  Collections.unmodifiableList(banknoteBatches);
    }

    @Override public boolean equals(Object other) {
        if (!(other instanceof Order)) {
            return false;
        }
        return id == ((Order)other).id;
    }

    @Override public String toString() {
        return id.toString();
    }

    public void AddBatch(BanknoteBatch banknoteBatch) {
        banknoteBatches.add(banknoteBatch);
    }

    public boolean IsCompleted() {
        return GetRequiredAmount() == 0;
    }
}