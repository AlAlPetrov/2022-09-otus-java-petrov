package ru.otus;

import ru.otus.Exceptions.CashBoxException;
import ru.otus.Exceptions.DispenserException;
import ru.otus.Orders.DepositeOrder;
import ru.otus.Orders.WithdrawOrder;
import ru.otus.Orders.CashReportOrder;

import java.util.List;

public class Atm {
    private IDispenser dispenser;
    private IPrinter printer;

    private Atm () {};
    public Atm(IDispenser dispenser,
               IPrinter printer) {
        this.dispenser = dispenser;
        this.printer = printer;
    }

    public void Withdraw(int amount) throws DispenserException, CashBoxException {
        var order = new WithdrawOrder(amount);
        dispenser.Withdraw(order);
        printer.Print(order);
    }

    public void Deposite(List<BanknoteBatch> banknoteBatches) throws CashBoxException, DispenserException {
        var order = new DepositeOrder();
        dispenser.Deposite(order, banknoteBatches);
    }

    public void CashReport() {
        var order = new CashReportOrder();
        dispenser.CashReport(order);
        printer.Print(order);
    }
}