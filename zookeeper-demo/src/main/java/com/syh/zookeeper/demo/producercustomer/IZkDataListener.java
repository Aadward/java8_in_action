package com.syh.zookeeper.demo.producercustomer;

@FunctionalInterface
public interface IZkDataListener {

    void handleDataChanged(String nodePath, byte[] data);
}
