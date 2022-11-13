package ru.otus.Orders;

public class WithdrawOrder extends Order {
    public WithdrawOrder(int amount) {
        super(amount);
    }

    @Override
    public String toString() {
        return "Withdraw: " + id.toString()
                + ", requested: " + requestedAmount;
    }
}
