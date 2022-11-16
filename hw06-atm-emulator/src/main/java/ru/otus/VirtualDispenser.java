package ru.otus;

import ru.otus.Exceptions.CashBoxException;
import ru.otus.Exceptions.DispenserException;
import ru.otus.Orders.DispenserResult;

import java.util.List;

public class VirtualDispenser implements Dispenser {
    private final List<CashBox> cashBoxes;

    public VirtualDispenser(List<CashBox> cashBoxes) {
        this.cashBoxes = cashBoxes;
    };

    @Override
    public void withdraw(DispenserResult order) throws DispenserException, CashBoxException {
        for (var cashBox :cashBoxes) {
            cashBox.reserve(order);
        }
        if (!order.isCompleted())
            throw new DispenserException("Failed to reserve requested cash");
        for (var cashBox :cashBoxes) {
            cashBox.withdraw(order);
        }
    }

    @Override
    public void deposit(DispenserResult order,
                        List<BanknoteBatch> banknoteBatches) throws CashBoxException, DispenserException {
        int deposited = 0;
        for (var cashBox: cashBoxes) {
            if (cashBox.depositOpen(order, banknoteBatches))
                deposited++;
        }
        if (deposited != banknoteBatches.size())
            throw new DispenserException("Failed to deposite, unsupported banknote type(s) detected");
        for (var cashBox: cashBoxes) {
            cashBox.depositCommit(order);
        }
    }

    @Override
    public void cashReport(DispenserResult order) {
        for (var cashBox: cashBoxes) {
            cashBox.cashReport(order);
        }
    }
}
