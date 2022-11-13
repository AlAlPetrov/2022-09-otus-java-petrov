package ru.otus.Orders;

public class DepositeOrder extends Order {
    public DepositeOrder() {
        super(0);
    }

    @Override
    public String toString() {
        return "deposite: " + id.toString();
    }
}
