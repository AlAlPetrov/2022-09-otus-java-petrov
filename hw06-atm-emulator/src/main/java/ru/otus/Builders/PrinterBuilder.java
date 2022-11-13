package ru.otus.Builders;

import ru.otus.Configuration;
import ru.otus.ConsolePrinter;
import ru.otus.IPrinter;

public class PrinterBuilder implements IPrinterBuilder {
    private Configuration configuration;

    private PrinterBuilder() {
    }
    public PrinterBuilder(Configuration configuration) {
        this.configuration = configuration;
    }
    public IPrinter Build(){
        return new ConsolePrinter();
    }
}
