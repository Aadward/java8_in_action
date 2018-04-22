package com.syh.zookeeper.demo.curator.lock.sharedlock;

import org.apache.curator.framework.recipes.locks.InterProcessLock;

public class Adder implements Runnable {

    static int n;

    private int count;

    private InterProcessLock lock;

    public Adder(int count, InterProcessLock lock) {
        this.count = count;
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            lock.acquire();
            int c = count;
            while (c-- > 0) {
                n += 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                lock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
