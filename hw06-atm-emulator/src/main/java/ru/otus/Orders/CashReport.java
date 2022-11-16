package ru.otus.Orders;

public class CashReport extends DispenserResult {
    public CashReport() {
        super(0);
    }

    @Override
    public String toString() {
        return "Cash report: " + id.toString();
    }
}
