package ru.otus;

import ru.otus.Exceptions.CashBoxException;
import ru.otus.Exceptions.DispenserException;
import ru.otus.Orders.Order;

import java.util.List;

public class Dispenser implements IDispenser {
    private final List<CashBox> cashBoxes;

    public Dispenser(List<CashBox> cashBoxes) {
        this.cashBoxes = cashBoxes;
    };

    @Override
    public void Withdraw(Order order) throws DispenserException, CashBoxException {
        for (var cashBox :cashBoxes) {
            cashBox.Reserve(order);
        }
        if (!order.IsCompleted())
            throw new DispenserException("Failed to reserve requested cash");
        for (var cashBox :cashBoxes) {
            cashBox.Withdraw(order);
        }
    }

    @Override
    public void Deposite(Order order,
                         List<BanknoteBatch> banknoteBatches) throws CashBoxException, DispenserException {
        int deposited = 0;
        for (var cashBox: cashBoxes) {
            if (cashBox.DepositeOpen(order, banknoteBatches))
                deposited++;
        }
        if (deposited != banknoteBatches.size())
            throw new DispenserException("Failed to deposite, unsupported banknote type(s) detected");
        for (var cashBox: cashBoxes) {
            cashBox.DepositeCommit(order);
        }
    }

    @Override
    public void CashReport(Order order) {
        for (var cashBox: cashBoxes) {
            cashBox.CashReport(order);
        }
    }
}
