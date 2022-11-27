package ru.otus;

import ru.otus.Exceptions.CashBoxException;
import ru.otus.Exceptions.DispenserException;

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
        var result = dispenser.withdraw(amount);
        printer.print(result);
    }

    public void deposit(List<BanknoteBatch> banknoteBatches) throws CashBoxException, DispenserException {
        dispenser.deposit(banknoteBatches);
    }

    public void cashReport() {
        var result = dispenser.cashReport();
        printer.print(result);
    }
}
