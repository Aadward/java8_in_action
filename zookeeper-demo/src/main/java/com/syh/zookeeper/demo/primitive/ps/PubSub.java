package com.syh.zookeeper.demo.primitive.ps;

import java.util.concurrent.TimeUnit;

import com.syh.zookeeper.demo.primitive.ZkClient;
import org.apache.zookeeper.KeeperException;

/**
 * This is a simple example of publisher and subscriber pattern
 * which works with zookeeper.
 *
 * @author syh
 */
public class PubSub {

    public static void main(String[] args) throws KeeperException, InterruptedException {
        ZkClient zkClient = new ZkClient("localhost:2181", 10000);

        zkClient.subscribe("/test", (path, data) -> {
            System.out.println("subscriber1: Node data changed");
            System.out.println("subscriber1: Value is: " + new String(data));
        });

        zkClient.subscribe("/test", (path, data) -> {
            System.out.println("subscriber2: Node data changed");
            System.out.println("subscriber2: Value is: " + new String(data));
        });


        int count = 0;
        while (count < 10) {
            zkClient.publish("/test", (count++) + "");
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
