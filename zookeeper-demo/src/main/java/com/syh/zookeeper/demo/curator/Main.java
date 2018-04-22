package com.syh.zookeeper.demo.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class Main {

    private static final String HOST = "localhost:2181";

    public static void main(String[] args) {
        CuratorFramework client = CuratorFrameworkFactory
                .newClient(HOST, new ExponentialBackoffRetry(1000, 10));

        client.start();
    }
}
