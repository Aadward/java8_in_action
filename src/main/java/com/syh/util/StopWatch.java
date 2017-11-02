package com.syh.util;


import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class StopWatch {

    private Stopwatch stopwatch = Stopwatch.createUnstarted();

    public void start() {
        stopwatch.reset();
        stopwatch.start();
    }

    public long getDuration(TimeUnit unit) {
        return stopwatch.elapsed(unit);
    }

    public void stop(){
        stop(TimeUnit.MILLISECONDS);
    }

    public void stop(TimeUnit unit) {
        String message = String.format("Time used: " + getDuration(unit) + " " + unit.toString());
        System.out.println(message);
        stopwatch.stop();
    }

    public static void main(String[] args) {
        StopWatch sw = new StopWatch();
        sw.start();
        sw.stop();
    }

}
