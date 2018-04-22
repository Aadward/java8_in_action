package com.syh.zookeeper.demo.curator.lock.sharedlock;

import com.syh.zookeeper.demo.curator.util.ClientUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.utils.CloseableUtils;

/**
 * A distribute shared lock based on zookeeper and not re-entrant.
 */
public class InterProcessSemaphoreMutexTest {

    private static final String servers = "localhost:2181";
    private static final String path = "/not_re_entrant_lock";

    static int N = 10;
    static int per = 10000;

    public static void main(String[] args) {
        CuratorFramework client = null;

        try {
            client = ClientUtil.newStartedClient(servers);
            InterProcessSemaphoreMutex lock = new InterProcessSemaphoreMutex(client, path);

            TestComponent lockTest = new TestComponent(lock, N, per);
            long result = lockTest.test();
            System.out.println(result == N * per);
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }
}
