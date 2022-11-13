package ru.otus;

/*
    java -javaagent:classLoader.jar -jar classLoader.jar
*/

import ru.otus.Exceptions.CashBoxException;
import ru.otus.Exceptions.DispenserException;
import ru.otus.Builders.AtmBuilder;
import ru.otus.Builders.DispenserBuilder;
import ru.otus.Builders.PrinterBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static final List<BanknoteBatch> banknoteBatches = new ArrayList<BanknoteBatch>() {{
        add(new BanknoteBatch(100, 100));
        add(new BanknoteBatch(200, 50));
        add(new BanknoteBatch(500, 1));
        add(new BanknoteBatch(1000, 5));
        add(new BanknoteBatch(5000, 2));
    }};

    public static void main(String[] args) throws DispenserException, CashBoxException {
        var configuration = new Configuration(Arrays.asList(100, 200, 500, 1000, 5000));
        var dispenserFactory = new DispenserBuilder(configuration);
        var printerFactory = new PrinterBuilder(configuration);

        var atm = new AtmBuilder(configuration,
                                    dispenserFactory,
                                    printerFactory)
                        .WithDispenser()
                        .WithPrinter()
                        .Build();
        atm.Deposite(banknoteBatches);
        atm.CashReport();
        atm.Withdraw(6800);
        atm.Withdraw(4800);
        atm.CashReport();
        //atm.Dispense(100000); //exception expected
    }
}
