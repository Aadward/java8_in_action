package com.syh.zookeeper.demo.curator.ps.path;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

public class Subscriber implements PathChildrenCacheListener {

    private PathChildrenCache cache;

    public Subscriber(PathChildrenCache cache) {
        this.cache = cache;
    }

    @Override
    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
        switch (event.getType()) {
            case CHILD_ADDED:
                System.out.println(String.format("Node %s created, data=%s",
                        event.getData().getPath(),
                        new String(event.getData().getData())));
                System.out.println("Do something, then remove the node");
                client.delete().forPath(event.getData().getPath());
                break;
            case CHILD_UPDATED:
                System.out.println(String.format("Node %s updated, data=%s",
                        event.getData().getPath(),
                        new String(event.getData().getData())));
                System.out.println("Do something, then remove the node");
                client.delete().forPath(event.getData().getPath());
                break;
            case CHILD_REMOVED:
                System.out.println(String.format("Node %s removed,",
                        event.getData().getPath()));
                break;
        }
    }
}
