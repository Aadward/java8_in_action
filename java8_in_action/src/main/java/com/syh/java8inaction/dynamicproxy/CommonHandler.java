package com.syh.java8inaction.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 代理类
 */
public class CommonHandler implements InvocationHandler {

    private Object target;

    public CommonHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("proxy object : " + proxy.getClass().getName());
        System.out.println("proxy method: " + method.getName());

        return method.invoke(target, args);
    }
}
