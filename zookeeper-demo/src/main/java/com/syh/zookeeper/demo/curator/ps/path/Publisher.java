package com.syh.zookeeper.demo.curator.ps.path;

import org.apache.curator.framework.CuratorFramework;

public class Publisher {

    private CuratorFramework client;

    private String parentPath;

    public Publisher(CuratorFramework client, String parentPath) {
        this.client = client;
        this.parentPath = parentPath;
    }

    public void publish(String childPath, String value) throws Exception {
        client.create()
                .orSetData()
                .creatingParentsIfNeeded()
                .forPath(parentPath + "/" + childPath, value.getBytes());
    }
}
