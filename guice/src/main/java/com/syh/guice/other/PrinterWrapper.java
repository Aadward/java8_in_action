package com.syh.guice.other;

import javax.inject.Inject;

public class PrinterWrapper {

    Printer printer;

    @Inject
    public PrinterWrapper(Printer printer) {
        this.printer = printer;
    }

    public void print() {
        printer.print();
    }
}
