package com.syh.java8inaction.trivial;

import com.google.common.base.Stopwatch;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

/**
 * 比AtomicLong性能高
 */
public class LongAdderTest {

    public static void main(String[] args) throws InterruptedException {
        long count = 1000000;
        int thread = 300;
        atomicIncrease(count, thread);
        longAdderIncrease(count, thread);
    }

    static void atomicIncrease(long count, int threadN) throws InterruptedException {
        Stopwatch sw = Stopwatch.createStarted();
        AtomicLong a = new AtomicLong();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(threadN, threadN, 1,TimeUnit.DAYS, new LinkedBlockingQueue<>());
        IntStream.range(0, threadN)
                .forEach(i -> executor.execute(() -> {
                    for (int j = 0; j < count; j ++) {
                        a.incrementAndGet();
                    }
                }));
        executor.shutdown();
        executor.awaitTermination(100, TimeUnit.HOURS);
        long used = sw.elapsed(TimeUnit.MILLISECONDS);
        System.out.println("Automic Long, value=" + a.get() + ", used=" + used + "microseconds");
    }

    static void longAdderIncrease(long count, int threadN) throws InterruptedException {
        Stopwatch sw = Stopwatch.createStarted();
        LongAdder a = new LongAdder();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(threadN, threadN, 1,TimeUnit.DAYS, new LinkedBlockingQueue<>());
        IntStream.range(0, threadN)
                .forEach(i -> executor.execute(() -> {
                    for (int j = 0; j < count; j ++) {
                        a.increment();
                    }
                }));
        executor.shutdown();
        executor.awaitTermination(100, TimeUnit.HOURS);
        long used = sw.elapsed(TimeUnit.MILLISECONDS);
        System.out.println("Automic Long, value=" + a.longValue() + ", used=" + used + "microseconds");
    }
}
