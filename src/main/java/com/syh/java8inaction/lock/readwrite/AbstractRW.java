package com.syh.java8inaction.lock.readwrite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.base.Stopwatch;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class AbstractRW {

    protected long count = 0;
    protected long goal = 1000 * 1000 * 10;

    private int writeThreadNum = 2;
    private int readThreadNum = 16;

    protected AbstractRW(long goal, int writeThreadNum, int readThreadNum) {
        this.goal = goal;
        this.writeThreadNum = writeThreadNum;
        this.readThreadNum = readThreadNum;
        readCount = new long[readThreadNum];
    }

    private long[] readCount = new long[readThreadNum];
    private long mills;


    private long getTotalReadCount() {
        return Arrays.stream(readCount)
                .reduce(0, (a, b) -> (a + b));
    }

    abstract protected long write();

    abstract protected long read();


    public void start() {
        List<Thread> all = new ArrayList<>();

        all.addAll(
                IntStream.range(0, writeThreadNum)
                .mapToObj(i -> new Thread(() -> {
                    while (write() < goal);
                }))
                .collect(Collectors.toList())
        );

        all.addAll(
                IntStream.range(0, readThreadNum)
                .mapToObj(i -> new Thread(() -> {
                    while (read() < goal) {
                        readCount[i] += 1;
                    }
                })).collect(Collectors.toList())
        );

        Stopwatch sw = Stopwatch.createStarted();

        all.forEach(t -> t.start());
        all.forEach(this::waitFor);

        this.mills = sw.elapsed(TimeUnit.MILLISECONDS);
    }

    private void waitFor(Thread t) {
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public String getConclusion() {
        return String.format("Goal is %d, count is %d\nTotal read %d times\nTotal use time %d millseconds",
                goal, count, getTotalReadCount(), this.mills);
    }

}
