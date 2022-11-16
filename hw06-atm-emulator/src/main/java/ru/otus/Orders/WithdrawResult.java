package ru.otus.Orders;

public class WithdrawResult extends DispenserResult {
    public WithdrawResult(int amount) {
        super(amount);
    }

    @Override
    public String toString() {
        return "Withdraw: " + id.toString()
                + ", requested: " + requestedAmount;
    }
}
