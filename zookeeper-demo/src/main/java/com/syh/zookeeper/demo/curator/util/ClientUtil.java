package com.syh.zookeeper.demo.curator.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ClientUtil {

    public static CuratorFramework newStartedClient(String servers) {
        CuratorFramework client = CuratorFrameworkFactory.newClient(servers,
                new ExponentialBackoffRetry(1000, 10));
        client.start();
        return client;
    }
}
