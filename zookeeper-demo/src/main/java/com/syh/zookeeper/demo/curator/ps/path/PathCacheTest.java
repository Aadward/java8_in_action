package com.syh.zookeeper.demo.curator.ps.path;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.concurrent.TimeUnit;

public class PathCacheTest {

    private static final String path = "/test/path_pb";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = null;
        PathChildrenCache cache = null;

        try {
            client = CuratorFrameworkFactory.newClient("localhost:2181",
                    new ExponentialBackoffRetry(1000, 10));
            client.start();

            cache = new PathChildrenCache(client, path, true);

            cache.start();
            //cache.rebuild();

            Subscriber subscriber = new Subscriber(cache);
            cache.getListenable().addListener(subscriber);
            Publisher publisher = new Publisher(client, path);

            int count = 10;
            while (count-- > 0) {
                publisher.publish(count + "", "data" + count);
                //Because of re-watcher mechanism, some changes can not be noticed.
                //So sleep for a while.
                TimeUnit.SECONDS.sleep(1);
            }
        } finally {
            CloseableUtils.closeQuietly(cache);
            CloseableUtils.closeQuietly(client);
        }
    }
}
