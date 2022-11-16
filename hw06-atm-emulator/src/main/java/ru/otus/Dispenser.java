package ru.otus;

import ru.otus.Exceptions.CashBoxException;
import ru.otus.Exceptions.DispenserException;
import ru.otus.Orders.DispenserResult;

import java.util.List;

public interface Dispenser {
    void withdraw(DispenserResult order) throws DispenserException, CashBoxException;
    void deposit(DispenserResult order,
                 List<BanknoteBatch> banknoteBatches) throws CashBoxException, DispenserException;
    void cashReport(DispenserResult order);
}
