package ru.otus.Builders;

import ru.otus.Atm;
import ru.otus.Configuration;

public class AtmBuilder {
    private Configuration configuration;
    private DispenserFactory dispenserFactory;
    private PrinterFactory printerFactory;
//    private boolean withDispenser;
  //  private boolean withPrinter;

    private AtmBuilder(){
    };
    public AtmBuilder(Configuration configuration) {
        this.configuration = configuration;
    }
    public AtmBuilder withDispenser(DispenserFactory dispenserFactory){
        this.dispenserFactory = dispenserFactory;
        return this;
    }
    public AtmBuilder withPrinter(PrinterFactory printerFactory){
        this.printerFactory = printerFactory;
        return this;
    }
    public Atm build(){
        var dispenser = dispenserFactory != null ? dispenserFactory.build():null;
        var printer = printerFactory != null ? printerFactory.build():null;
        return new Atm(dispenser, printer);
    }
}
