package com.syh.zookeeper.demo.curator.ps.nodecache;

import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;

public class Subscriber implements NodeCacheListener {

    private NodeCache nodeCache;

    public Subscriber(NodeCache cache) {
        this.nodeCache = cache;
    }

    @Override
    public void nodeChanged() throws Exception {
        ChildData data = nodeCache.getCurrentData();
        String nodePath = nodeCache.getPath();
        if (data == null) {
            //deleted
            System.out.println(String.format("Node %s has deleted", nodePath));
        } else {
            // create, update
            System.out.println(String.format("Node %s has changed, current data is %s",
                    nodePath, new String(data.getData())));
        }
    }
}
