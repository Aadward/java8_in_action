package com.syh.zookeeper.demo.primitive.connect;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * A simple example of build connection synchronously.
 *
 * @author syh
 */
public class ConnectWatcher implements Watcher {

    static CountDownLatch lock = new CountDownLatch(1);

    public static void main(String[] args) throws IOException {
        ZooKeeper zooKeeper = new ZooKeeper("localhost:2181",
                5000,
                new ConnectWatcher());
        System.out.println(zooKeeper.getState());
        try {
            lock.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Zookeeper connection established");
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("====== Receive event ====== :" + watchedEvent);
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            lock.countDown();
        }
    }
}
