package ru.otus.Orders;

public class DepositResult extends DispenserResult {
    public DepositResult() {
        super(0);
    }

    @Override
    public String toString() {
        return "deposite: " + id.toString();
    }
}
