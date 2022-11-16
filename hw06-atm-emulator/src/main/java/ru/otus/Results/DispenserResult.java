package ru.otus.Results;

import ru.otus.BanknoteBatch;
import ru.otus.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DispenserResult {
    private final Transaction transaction;
    private final int requestedAmount;
    private List<BanknoteBatch> banknoteBatches = new ArrayList<BanknoteBatch>();
    public DispenserResult(int amount, Transaction transaction) {
        this.requestedAmount = amount;
        this.transaction = transaction;
    }

    public int getRequestedAmount(){
        return requestedAmount;
    }
    public Transaction getTransaction(){
        return transaction;
    }
    public List<BanknoteBatch> getBanknoteBatches() {
        return  Collections.unmodifiableList(banknoteBatches);
    }
    public void addBatch(BanknoteBatch banknoteBatch) {
        banknoteBatches.add(banknoteBatch);
    }

    @Override public String toString() {
        return transaction.toString();
    }
}
