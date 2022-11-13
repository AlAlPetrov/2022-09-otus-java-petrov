package ru.otus.Builders;

import ru.otus.Atm;
import ru.otus.Configuration;

public class AtmBuilder {
    private Configuration configuration;
    private IDispenserBuilder dispenserFactory;
    private IPrinterBuilder printerFactory;
    private boolean withDispenser;
    private boolean withPrinter;

    private AtmBuilder(){
    };
    public AtmBuilder(Configuration configuration,
                      IDispenserBuilder dispenserFactory,
                      IPrinterBuilder printerFactory) {
        this.configuration = configuration;
        this.dispenserFactory = dispenserFactory;
        this.printerFactory = printerFactory;
    }
    public AtmBuilder WithDispenser(){
        withDispenser = true;
        return this;
    }
    public AtmBuilder WithPrinter(){
        withPrinter = true;
        return this;
    }
    public Atm Build(){
        var dispenser = withDispenser? dispenserFactory.Build():null;
        var printer = withPrinter? printerFactory.Build():null;
        return new Atm(dispenser,
                printer);
    }
}
