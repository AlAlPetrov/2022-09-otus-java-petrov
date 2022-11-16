package ru.otus;

import ru.otus.Results.DispenserResult;

import java.util.List;

public interface Dispenser {
    DispenserResult withdraw(int amount) throws RuntimeException;
    DispenserResult deposit(List<BanknoteBatch> banknoteBatches) throws RuntimeException;
    DispenserResult cashReport();
}
