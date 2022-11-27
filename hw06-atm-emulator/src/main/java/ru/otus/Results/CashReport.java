package ru.otus.Results;

import ru.otus.Transaction;

public class CashReport extends DispenserResult {
    public CashReport(Transaction transaction) {
        super(0, transaction);
    }

    @Override
    public String toString() {
        return "Cash report: " + getTransaction().id().toString();
    }
}
