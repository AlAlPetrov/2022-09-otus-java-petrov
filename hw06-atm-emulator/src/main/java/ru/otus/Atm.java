package ru.otus;

import ru.otus.Exceptions.CashBoxException;
import ru.otus.Exceptions.DispenserException;
import ru.otus.Orders.DepositResult;
import ru.otus.Orders.WithdrawResult;
import ru.otus.Orders.CashReport;

import java.util.List;

public class Atm {
    private Dispenser dispenser;
    private Printer printer;

    private Atm () {};
    public Atm(Dispenser dispenser,
               Printer printer) {
        this.dispenser = dispenser;
        this.printer = printer;
    }

    public void withdraw(int amount) throws DispenserException, CashBoxException {
        var order = new WithdrawResult(amount);
        dispenser.withdraw(order);
        printer.print(order);
    }

    public void deposit(List<BanknoteBatch> banknoteBatches) throws CashBoxException, DispenserException {
        var order = new DepositResult();
        dispenser.deposit(order, banknoteBatches);
    }

    public void cashReport() {
        var order = new CashReport();
        dispenser.cashReport(order);
        printer.print(order);
    }
}
