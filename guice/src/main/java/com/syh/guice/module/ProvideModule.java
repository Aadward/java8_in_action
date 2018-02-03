package com.syh.guice.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.syh.guice.other.Printer;
import com.syh.guice.provider.PrinterProvider;


/**
 * 两种方式都可以引入依赖
 */
public class ProvideModule extends AbstractModule {
    @Override
    protected void configure() {
        //bind(Printer.class).toProvider(PrinterProvider.class);
    }

    @Provides
    Printer getPrinter() {
        return new Printer();
    }
}
