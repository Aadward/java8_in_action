package com.syh.java8inaction.lock;

public class SynchronizedRW extends AbstractRW {

    public SynchronizedRW() {}

    public SynchronizedRW(long goal, int writeThreadNum, int readThreadNum) {
        super(goal, writeThreadNum, readThreadNum);
    }

    @Override
    protected long write() {
        synchronized (this) {
            if (count < goal) {
                count += 1;
            }
            return count;
        }
    }

    @Override
    protected long read() {
        synchronized (this) {
            return count;
        }
    }
}
