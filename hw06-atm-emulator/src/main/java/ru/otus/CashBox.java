package ru.otus;

import ru.otus.Exceptions.CashBoxException;

import java.util.List;

public class CashBox {
    private final int maxCount = 100;
    private int banknoteValue;
    private int pendingBanknoteCount;
    private int remainingCount;
    private Transaction transaction;

    public CashBox(int banknoteValue){
        this.banknoteValue = banknoteValue;
    }

    private void resetPendingOperation() {
        pendingBanknoteCount = 0;
        transaction = null;
    }

    private void putBanknotes(BanknoteBatch banknoteBatch) throws CashBoxException {
        if (remainingCount + pendingBanknoteCount + banknoteBatch.count() > maxCount)
            throw new CashBoxException("banknote count must not exceed " + maxCount);
        pendingBanknoteCount = banknoteBatch.count();
    }

    public boolean depositOpen(Transaction transaction, List<BanknoteBatch> banknoteBatches) throws CashBoxException {
        resetPendingOperation();
        this.transaction = transaction;
        for (var banknoteBatch: banknoteBatches) {
            if (banknoteBatch.banknoteValue() == banknoteValue) {
                putBanknotes(banknoteBatch);
                return true;
            }
        }
        return false;
    }

    public void depositCommit(Transaction transaction) throws RuntimeException {
        if (this.transaction != transaction)
            throw new CashBoxException("deposit started with different order" + transaction);
        remainingCount += pendingBanknoteCount;
        resetPendingOperation();
    }

    public BanknoteBatch reserve(Transaction transaction, int amount) {
        resetPendingOperation();
        this.transaction = transaction;
        pendingBanknoteCount = Math.min((int)(amount / banknoteValue), remainingCount);
        return new BanknoteBatch(banknoteValue, pendingBanknoteCount);
    }

    public void withdraw(Transaction transaction) throws RuntimeException {
        if (this.transaction != transaction)
            throw new CashBoxException("reserved order differs from" + transaction);
        remainingCount -= pendingBanknoteCount;
        resetPendingOperation();
    }

    public BanknoteBatch cashReport() {
        return new BanknoteBatch(banknoteValue, remainingCount);
    }
}
