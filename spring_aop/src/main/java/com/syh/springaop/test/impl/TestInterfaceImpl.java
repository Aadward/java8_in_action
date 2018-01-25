package com.syh.springaop.test.impl;

import com.syh.springaop.test.TestInterface;
import org.springframework.stereotype.Repository;

@Repository
public class TestInterfaceImpl implements TestInterface {

    @Override
    public void doNothing(String message) {
        //do nothing
    }

    @Override
    public String get(int id) {
        return "test";
    }
}
