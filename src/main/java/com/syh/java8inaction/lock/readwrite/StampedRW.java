package com.syh.java8inaction.lock.readwrite;

import java.util.concurrent.locks.StampedLock;

public class StampedRW extends AbstractRW {

    private StampedLock lock = new StampedLock();

    public StampedRW() {}

    public StampedRW(long goal, int writeThreadNum, int readThreadNum) {
        super(goal, writeThreadNum, readThreadNum);
    }

    @Override
    protected long write() {
        long stamp = lock.writeLock();
        try {
            if (count < goal) {
                count += 1;
            }
            return count;
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    @Override
    protected long read() {
        long stamp = lock.tryOptimisticRead();
        long mCount = count;
        if (!lock.validate(stamp)) {
            stamp = lock.readLock();
            try {
                mCount = count;
            } finally {
                lock.unlockRead(stamp);
            }
        }
        return mCount;
    }
}
