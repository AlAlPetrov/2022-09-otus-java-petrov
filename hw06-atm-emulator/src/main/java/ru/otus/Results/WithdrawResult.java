package ru.otus.Results;

import ru.otus.Transaction;

public class WithdrawResult extends DispenserResult {
    public WithdrawResult(int amount, Transaction transaction) {
        super(amount, transaction);
    }

    @Override
    public String toString() {
        return "Withdraw: " + getTransaction().id().toString()
                + ", requested: " + getRequestedAmount();
    }
}
