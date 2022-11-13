package ru.otus.Orders;

public class CashReportOrder extends Order {
    public CashReportOrder() {
        super(0);
    }

    @Override
    public String toString() {
        return "Cash report: " + id.toString();
    }
}
