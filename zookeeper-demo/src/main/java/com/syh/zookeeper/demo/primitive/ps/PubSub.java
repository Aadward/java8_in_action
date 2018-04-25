package com.syh.zookeeper.demo.primitive.ps;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.syh.zookeeper.demo.primitive.IZkDataListener;
import com.syh.zookeeper.demo.primitive.ZkClient;

/**
 * This is a simple example of publisher and subscriber pattern
 * which works with zookeeper.
 *
 * If many changes happen in a short time, some changes may not notify the listeners
 * registered, but you can always get the newest data.
 *
 * @author Yuhang Shen
 */
public class PubSub {

    public static void main(String[] args) throws InterruptedException {
        ZkClient zkClient = null;
        try {
            zkClient = ZkClient.createClient("localhost:2181");
            zkClient.subscribeDataChanged("/test", new IZkDataListener() {
                @Override
                public void onNodeDataChanged(String path, Optional<String> data) {
                    System.out.println("Node " + path + " changed, value = " + data.orElse(""));
                }

                @Override
                public void onNodeDeleted(String path) {
                    System.out.println("Node " + path + " deleted");
                }
            });

            int count = 5;
            while (count-- > 0) {
                zkClient.setData("/test", (count + "").getBytes(), true);
                TimeUnit.SECONDS.sleep(1);
            }
        } finally {
            if (zkClient != null) {
                zkClient.close();
            }
        }

    }
}
