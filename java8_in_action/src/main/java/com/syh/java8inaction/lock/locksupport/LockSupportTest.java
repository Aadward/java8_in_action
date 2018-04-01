package com.syh.java8inaction.lock.locksupport;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LockSupportTest {

    AtomicBoolean isLocked = new AtomicBoolean(false);
    BlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();

    public void lock() {
        boolean interrupted = false;

        Thread current = Thread.currentThread();
        waiters.add(current);

        while (waiters.peek() != current ||
                !isLocked.compareAndSet(false, true)) {
            //阻止当前线程的调度
            LockSupport.park(this);
            //因为park也要响应中断，判断是否被其他线程中断并且将结果保存在临时变量中
            //isInterrupt()会把中断状态置为false
            if (current.isInterrupted()) {
                interrupted = true;
            }
        }

        waiters.remove();

        //重新设置中断状态
        if (interrupted) {
            current.interrupt();
        }
    }

    public void unlock() {
        isLocked.set(false);
        LockSupport.unpark(waiters.peek());
    }


    /**
     * 下面是测试的代码，10个线程同时对循环进行+1操作
     */

    public static int i = 0;
    public static void increase() {
        LockSupportTest.i = LockSupportTest.i + 1;
    }
    public static void main(String[] args) {
        LockSupportTest lock = new LockSupportTest();
        List<Thread> threads = IntStream.range(0, 10)
                .mapToObj(i -> new Thread(() -> {
                    int count = 100;
                    while (count-- > 0) {
                        try {
                            lock.lock();
                            increase();
                        } finally {
                            lock.unlock();
                        }
                    }
                }))
                .collect(Collectors.toList());
        threads.forEach(t -> t.start());
        threads.forEach(t -> waitForFinish(t));

        System.out.println(i);

    }

    private static void waitForFinish(Thread t) {
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
