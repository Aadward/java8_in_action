package com.syh.java8inaction.safepublish;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * 臭名昭著的Double Lock Check，在本地环境下无法复现.
 */
public class DoubleCheck {

    Object obj;

    public Object lazyInitial() {
        if (obj == null) {
            synchronized (this) {
                if (obj == null) {
                    this.obj = new Object();
                }
            }
        }
        return obj;
    }

    public static void main(String[] args) {
        DoubleCheck doubleCheck = new DoubleCheck();
        ExecutorService executor = Executors.newCachedThreadPool();

        long ret = IntStream.range(0, 100000)
                .boxed()
                .map(i -> executor.submit(() -> doubleCheck.lazyInitial()))
                .map(future -> {
                    try {
                        return future.get().hashCode();
                    } catch (Exception e) {
                        return -1;
                    }
                })
                .distinct()
                .count();

        executor.shutdown();

        System.out.println(ret);
    }
}
