package ru.otus;

import ru.otus.Results.DispenserResult;

public class ConsolePrinter implements Printer {

    @Override
    public void print(DispenserResult order) {
        System.out.println("***************");
        System.out.println(order.toString());
        System.out.println("***************");

        System.out.println("banknotes: ");
        for (var banknoteBatch : order.getBanknoteBatches()) {
            System.out.println(banknoteBatch.banknoteValue() + "x" + banknoteBatch.count());
        }
        System.out.println("***************");
        System.out.println();
    }
}
