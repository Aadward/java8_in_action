package com.syh.zookeeper.demo.curator.ps.nodecache;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.concurrent.TimeUnit;

public class PubSubTest {

    private static final String path = "/test/node_pb";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = null;
        NodeCache nodeCache = null;

        try {
            client = CuratorFrameworkFactory.newClient("localhost:2181",
                    new ExponentialBackoffRetry(1000, 10));
            client.start();

            nodeCache = new NodeCache(client, path);

            Subscriber subscriber = new Subscriber(nodeCache);
            nodeCache.getListenable().addListener(subscriber);
            Publisher publisher = new Publisher(nodeCache);

            nodeCache.start();
            nodeCache.rebuild();

            int count = 10;
            while (count-- > 0) {
                publisher.publish(count + "");
                //Because of re-watcher mechanism, some changes can not be noticed.
                //So sleep for a while.
                TimeUnit.SECONDS.sleep(1);
            }
        } finally {
            CloseableUtils.closeQuietly(nodeCache);
            CloseableUtils.closeQuietly(client);
        }
    }
}
