package ru.otus.Results;

import ru.otus.Transaction;

public class DepositResult extends DispenserResult {
    public DepositResult(Transaction transaction) {
        super(0, transaction);
    }

    @Override
    public String toString() {
        return "deposit: " + getTransaction().id().toString();
    }
}
