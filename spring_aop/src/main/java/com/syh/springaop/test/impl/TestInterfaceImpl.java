package com.syh.springaop.test.impl;

import com.syh.springaop.test.TestInterface;
import org.springframework.stereotype.Repository;

@Repository
public class TestInterfaceImpl implements TestInterface {

    @Override
    public void doNothing(String message) {
        System.out.println("Invoke doNothing()");
    }

    @Override
    public String get(int id) {
        System.out.println("Invoke get()");
        return "test";
    }

    @Override
    public void throwException() {
        System.out.println("Invoke throwException()");
    }
}
