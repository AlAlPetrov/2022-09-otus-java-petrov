package ru.otus;

import ru.otus.Exceptions.CashBoxException;
import ru.otus.Exceptions.DispenserException;
import ru.otus.Orders.Order;

import java.util.List;

public interface IDispenser {
    void Withdraw(Order order) throws DispenserException, CashBoxException;
    void Deposite(Order order,
                  List<BanknoteBatch> banknoteBatches) throws CashBoxException, DispenserException;
    void CashReport(Order order);
}
