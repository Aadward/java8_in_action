package com.syh.java8inaction.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteRW extends AbstractRW{

    public ReadWriteRW() {}

    public ReadWriteRW(long goal, int writeThreadNum, int readThreadNum) {
        super(goal, writeThreadNum, readThreadNum);
    }

    private ReadWriteLock lock = new ReentrantReadWriteLock(false);

    private Lock writeLock = lock.writeLock();
    private Lock readLock = lock.readLock();

    @Override
    protected long write() {
        writeLock.lock();
        try {
            if (count < goal) {
                count += 1;
            }
            return count;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    protected long read() {
        readLock.lock();
        try {
            return count;
        } finally {
            readLock.unlock();
        }
    }
}
