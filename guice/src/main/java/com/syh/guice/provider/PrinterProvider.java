package com.syh.guice.provider;

import com.syh.guice.other.Printer;

import javax.inject.Provider;

public class PrinterProvider implements Provider<Printer> {

    @Override
    public Printer get() {
        return new Printer();
    }
}
