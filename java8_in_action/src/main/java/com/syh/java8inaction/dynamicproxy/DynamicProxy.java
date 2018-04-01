package com.syh.java8inaction.dynamicproxy;

import java.lang.reflect.Proxy;

public class DynamicProxy {
    public static void main(String[] args) {
        IProducer producer = getProducer(new ProducerImpl());
        System.out.println(producer.produce());
    }


    /**
     * 组装代理类，JDK自带的动态代理依赖于接口实现
     */
    public static IProducer getProducer(IProducer consignor) {
        return (IProducer) Proxy.newProxyInstance(DynamicProxy.class.getClassLoader(),
                                                  new Class[]{IProducer.class},
                                                  new CommonHandler(consignor));
    }
}
