package ru.otus;

import ru.otus.Exceptions.CashBoxException;
import ru.otus.Orders.DispenserResult;

import java.util.List;

public class CashBox {
    private final int maxCount = 100;
    private int banknoteValue;
    private int pendingBanknoteCount;
    private DispenserResult order;
    private int remainingCount;

    public CashBox(int banknoteValue){
        this.banknoteValue = banknoteValue;
    }

    private void resetPendingOperation() {
        pendingBanknoteCount = 0;
        order = null;
    }
    private void putBanknotes(BanknoteBatch banknoteBatch) throws CashBoxException {
        if (remainingCount + pendingBanknoteCount + banknoteBatch.count() > maxCount)
            throw new CashBoxException("banknote count must not exceed " + maxCount);
        pendingBanknoteCount = banknoteBatch.count();
    }

    public boolean depositOpen(DispenserResult order, List<BanknoteBatch> banknoteBatches) throws CashBoxException {
        resetPendingOperation();
        this.order = order;
        for (var banknoteBatch: banknoteBatches) {
            if (banknoteBatch.banknoteValue() == banknoteValue) {
                putBanknotes(banknoteBatch);
                return true;
            }
        }
        return false;
    }
    public void depositCommit(DispenserResult order) throws CashBoxException {
        if (this.order != order)
            throw new CashBoxException("deposit started with different order" + order);
        remainingCount += pendingBanknoteCount;
        resetPendingOperation();
    }

    public void reserve(DispenserResult order) {
        resetPendingOperation();
        this.order = order;
        pendingBanknoteCount = Math.min((int)(order.getRequiredAmount() / banknoteValue), remainingCount);
        order.addBatch(new BanknoteBatch(banknoteValue, pendingBanknoteCount));
    }

    public void withdraw(DispenserResult order) throws CashBoxException {
        if (this.order != order)
            throw new CashBoxException("reserved order differs from" + order);
        remainingCount -= pendingBanknoteCount;
        resetPendingOperation();
    }

    public void cashReport(DispenserResult order) {
        order.addBatch(new BanknoteBatch(banknoteValue, remainingCount));
    }
}
