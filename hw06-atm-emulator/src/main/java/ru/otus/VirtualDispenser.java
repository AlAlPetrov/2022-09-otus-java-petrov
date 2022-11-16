package ru.otus;

import ru.otus.Exceptions.DispenserException;
import ru.otus.Results.CashReport;
import ru.otus.Results.DepositResult;
import ru.otus.Results.DispenserResult;
import ru.otus.Results.WithdrawResult;

import java.util.List;

public class VirtualDispenser implements Dispenser {
    private final List<CashBox> cashBoxes;

    public VirtualDispenser(List<CashBox> cashBoxes) {
        this.cashBoxes = cashBoxes;
    };

    @Override
    public DispenserResult withdraw(int requestedAmount) throws RuntimeException {
        var transaction = new Transaction();
        var result = new WithdrawResult(requestedAmount, transaction);
        for (var cashBox :cashBoxes) {
            var banknoteBatch = cashBox.reserve(transaction, requestedAmount);
            requestedAmount -= banknoteBatch.total();
            result.addBatch(banknoteBatch);
        }
        if (requestedAmount != 0)
            throw new DispenserException("Failed to reserve requested cash");
        for (var cashBox :cashBoxes) {
            cashBox.withdraw(transaction);
        }
        return  result;
    }

    @Override
    public DispenserResult deposit(List<BanknoteBatch> banknoteBatches) throws RuntimeException {
        int deposited = 0;
        var transaction = new Transaction();
        var result = new DepositResult(transaction);
        for (var cashBox: cashBoxes) {
            if (cashBox.depositOpen(transaction, banknoteBatches))
                deposited++;
        }
        if (deposited != banknoteBatches.size())
            throw new DispenserException("Failed to deposit, unsupported banknote type(s) detected");
        for (var cashBox: cashBoxes) {
            cashBox.depositCommit(transaction);
        }
        return result;
    }

    @Override
    public DispenserResult cashReport() {
        var report = new CashReport(new Transaction());
        for (var cashBox: cashBoxes) {
            report.addBatch(cashBox.cashReport());
        }
        return report;
    }
}
