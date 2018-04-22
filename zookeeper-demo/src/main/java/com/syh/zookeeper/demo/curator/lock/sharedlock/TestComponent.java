package com.syh.zookeeper.demo.curator.lock.sharedlock;

import org.apache.curator.framework.recipes.locks.InterProcessLock;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestComponent {

    private InterProcessLock lock;

    private int nAdder;
    private int countPerAdder;

    public TestComponent(InterProcessLock lock, int nAdder, int countPerAdder) {
        this.lock = lock;
        this.nAdder = nAdder;
        this.countPerAdder = countPerAdder;
    }

    public int test() {
        Adder.n = 0;
        List<Thread> threads = IntStream.range(0, nAdder)
                .boxed()
                .map(i -> new Adder(countPerAdder, lock))
                .map(Thread::new)
                .peek(Thread::start)
                .collect(Collectors.toList());

        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return Adder.n;
    }
}
