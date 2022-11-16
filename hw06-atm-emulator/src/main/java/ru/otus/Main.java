package ru.otus;

/*
    java -jar atm-emulator.jar
*/

import ru.otus.Exceptions.CashBoxException;
import ru.otus.Exceptions.DispenserException;
import ru.otus.Builders.AtmBuilder;
import ru.otus.Builders.VirtualDispenserFactory;
import ru.otus.Builders.ConsolePrinterFactory;

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
        var dispenserFactory = new VirtualDispenserFactory(configuration);
        var printerFactory = new ConsolePrinterFactory(configuration);

        var atm = new AtmBuilder(configuration)
                        .withDispenser(dispenserFactory)
                        .withPrinter(printerFactory)
                        .build();
        atm.deposit(banknoteBatches);
        atm.cashReport();
        atm.withdraw(6800);
        atm.withdraw(4800);
        atm.cashReport();
        //atm.Dispense(100000); //exception expected
    }
}
