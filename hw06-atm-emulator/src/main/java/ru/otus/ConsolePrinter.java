package ru.otus;

import ru.otus.Orders.Order;

public class ConsolePrinter implements IPrinter {
    @Override
    public void Print(Order order) {
        System.out.println("***************");
        System.out.println(order.toString());
        System.out.println("***************");

        System.out.println("banknotes: ");
        for (var banknoteBatch : order.GetBanknoteBatches()) {
            System.out.println(banknoteBatch.banknoteValue() + "x" + banknoteBatch.count());
        }
        System.out.println("***************");
        System.out.println();
    }
}
