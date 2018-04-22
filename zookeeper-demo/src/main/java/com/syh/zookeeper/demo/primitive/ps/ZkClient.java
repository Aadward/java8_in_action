package com.syh.zookeeper.demo.primitive.ps;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * @author syh
 */

public class ZkClient {

    private String zkServers;
    private int sessionTimeout;

    private ZooKeeper zk;

    public ZkClient(String zkServers, int sessionTimeout) {
        this.zkServers = zkServers;
        this.sessionTimeout = sessionTimeout;
        connect();
    }

    private void connect() {
        CountDownLatch waitLatch = new CountDownLatch(1);
        try {
            zk = new ZooKeeper(zkServers, sessionTimeout,
                    event -> {
                        System.out.println("Connect to zookeeper successful");
                        waitLatch.countDown();
                    }
            );
        } catch (IOException e) {
            System.out.println("Create zookeeper client fail because of network problem");
            e.printStackTrace();
        }
        while (zk == null || (zk != null && !zk.getState().isConnected())) {
            try {
                waitLatch.await();
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
                e.printStackTrace();
            }
        }
    }


    public boolean createNode(String nodePath, String value) {
        try {
            zk.create(nodePath,
                    value.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT);
        } catch (KeeperException.NodeExistsException e) {
            //ok
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void publish(String nodePath, String value) throws KeeperException, InterruptedException {
        Stat stat;
        try {
            stat = zk.setData(nodePath, value.getBytes(), -1);
        } catch (KeeperException.NoNodeException e) {
            createNode(nodePath, value);
            stat = zk.exists(nodePath, false);
        }
        if (stat == null) {
            throw new Error("Fail to watch");
        }
    }

    public void subscribe(String nodePath, IZkDataListener listener)
            throws KeeperException, InterruptedException {

        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                try {
                    zk.exists(event.getPath(), this);
                    if (event.getType() == Watcher.Event.EventType.NodeDataChanged) {
                        byte[] data = zk.getData(event.getPath(), false, new Stat());
                        listener.handleDataChanged(event.getPath(), data);
                    }
                } catch (InterruptedException | KeeperException e) {
                    e.printStackTrace();
                }
            }
        };

        Stat stat = zk.exists(nodePath, watcher);

        if (stat == null) {
            createNode(nodePath, "");
            stat = zk.exists(nodePath, watcher);
        }
        if (stat == null) {
            throw new Error("Fail to watch");
        }
    }
}
