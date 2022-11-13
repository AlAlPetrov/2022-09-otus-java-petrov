package ru.otus;

import ru.otus.Exceptions.CashBoxException;
import ru.otus.Orders.Order;

import java.util.List;

public class CashBox {
    private final int maxCount = 100;
    private int banknoteValue;
    private int pendingBanknoteCount;
    private Order order;
    private int remainingCount;

    public CashBox(int banknoteValue){
        this.banknoteValue = banknoteValue;
    }

    private void ResetPendingOperation() {
        pendingBanknoteCount = 0;
        order = null;
    }
    private void PutBanknotes(BanknoteBatch banknoteBatch) throws CashBoxException {
        if (remainingCount + pendingBanknoteCount + banknoteBatch.count() > maxCount)
            throw new CashBoxException("banknote count must not exceed " + maxCount);
        pendingBanknoteCount = banknoteBatch.count();
    }

    public boolean DepositeOpen(Order order, List<BanknoteBatch> banknoteBatches) throws CashBoxException {
        ResetPendingOperation();
        this.order = order;
        for (var banknoteBatch: banknoteBatches) {
            if (banknoteBatch.banknoteValue() == banknoteValue) {
                PutBanknotes(banknoteBatch);
                return true;
            }
        }
        return false;
    }
    public void DepositeCommit(Order order) throws CashBoxException {
        if (this.order != order)
            throw new CashBoxException("deposit started with different order" + order);
        remainingCount += pendingBanknoteCount;
        ResetPendingOperation();
    }

    public void Reserve(Order order) {
        ResetPendingOperation();
        this.order = order;
        pendingBanknoteCount = Math.min((int)(order.GetRequiredAmount() / banknoteValue), remainingCount);
        order.AddBatch(new BanknoteBatch(banknoteValue, pendingBanknoteCount));
    }

    public void Withdraw(Order order) throws CashBoxException {
        if (this.order != order)
            throw new CashBoxException("reserved order differs from" + order);
        remainingCount -= pendingBanknoteCount;
        ResetPendingOperation();
    }

    public void CashReport(Order order) {
        order.AddBatch(new BanknoteBatch(banknoteValue, remainingCount));
    }
}
