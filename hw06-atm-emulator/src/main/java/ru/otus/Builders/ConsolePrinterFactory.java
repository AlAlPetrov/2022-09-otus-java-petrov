package ru.otus.Builders;

import ru.otus.Configuration;
import ru.otus.ConsolePrinter;
import ru.otus.Printer;

public class ConsolePrinterFactory implements PrinterFactory {
    private Configuration configuration;

    private ConsolePrinterFactory() {
    }
    public ConsolePrinterFactory(Configuration configuration) {
        this.configuration = configuration;
    }
    public Printer build(){
        return new ConsolePrinter();
    }
}
