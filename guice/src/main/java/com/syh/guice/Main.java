package com.syh.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.syh.guice.module.ProvideModule;
import com.syh.guice.other.PrinterWrapper;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ProvideModule());
        injector.getInstance(PrinterWrapper.class).print();
    }
}
