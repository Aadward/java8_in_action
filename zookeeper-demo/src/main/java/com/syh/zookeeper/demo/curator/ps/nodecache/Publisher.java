package com.syh.zookeeper.demo.curator.ps.nodecache;

import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.zookeeper.data.Stat;

public class Publisher {

    private NodeCache nodeCache;

    public Publisher(NodeCache nodeCache) {
        this.nodeCache = nodeCache;
    }

    public void publish(String data) throws Exception {
        Stat stat = nodeCache.getClient().checkExists().forPath(nodeCache.getPath());
        if (stat == null) {
            nodeCache.getClient()
                    .create().forPath(nodeCache.getPath(), data.getBytes());
        } else {
            nodeCache.getClient()
                    .setData().forPath(nodeCache.getPath(), data.getBytes());
        }
    }
}
