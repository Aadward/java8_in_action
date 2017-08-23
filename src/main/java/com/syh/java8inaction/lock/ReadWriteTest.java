package com.syh.java8inaction.lock;


import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.base.Stopwatch;

public class ReadWriteTest {

    private static final long COUNT_GOAL = 1000 * 1000 * 10;

    private static final int WRITE_THREAD_NUM = 2;
    private static final int READ_THREAD_NUL = 16;

    private static final StampedLock sl = new StampedLock();

    private static long[] readNum;
    private static long count;

    static {
        readNum = new long[READ_THREAD_NUL];
        count = 0;
    }


    public static void init() {
        count = 0;
        readNum = new long[READ_THREAD_NUL];
    }


    public static long getTotalReadCount() {
        return Arrays.stream(readNum)
                .reduce(0, (a, b) -> a + b);
    }


    public static void main(String[] args) {
        long usedTime = stampedLockTest();
        System.out.println(String.format("StampedLock, goal=%d, used time=%d seconds, total read count=%d",
                COUNT_GOAL, usedTime, getTotalReadCount()));
    }

    public static long stampedLockTest() {
        init();

        List<Thread> writers = IntStream.range(0, WRITE_THREAD_NUM)
                .mapToObj(i -> new Thread(() -> {
                    while (true) {
                        long stamp = sl.writeLock();
                        try {
                            if (count >= COUNT_GOAL) {
                                return;
                            }
                            count += 1;
                        } finally {
                            sl.unlockWrite(stamp);
                        }
                    }
                }))
                .collect(Collectors.toList());

        List<Thread> readers = IntStream.range(0, READ_THREAD_NUL)
                .mapToObj(i -> new Thread(() -> {
                    Thread.currentThread().setName("Thread-" + i);
                    long readCount = 0;
                    while (true) {
                        long stamp = sl.tryOptimisticRead();
                        long mCount = count;
                        if (!sl.validate(stamp)) {
                            stamp = sl.readLock();
                            try {
                                mCount = count;
                            } finally {
                                sl.unlockRead(stamp);
                            }
                        }
                        readCount += 1;
                        if (mCount >= COUNT_GOAL) {
                            break;
                        }
                    }
                    readNum[i] = readCount;
                })).collect(Collectors.toList());

        Stopwatch sw = Stopwatch.createStarted();
        readers.forEach(reader -> reader.start());
        writers.forEach(writer -> writer.start());

        writers.forEach(writer -> {
            try {
                writer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        readers.forEach(reader -> {
            try {
                reader.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return sw.elapsed(TimeUnit.SECONDS);
    }

    public static long synchronizedTest() {

    }
}
