package com.syh.java8inaction.lock.readwrite;


public class ReadWriteTest {

    public static void main(String[] args) {

        System.out.println("===================ReadWriteLock=================");
        ReadWriteRW rwrw = new ReadWriteRW(1000 * 1000 * 10, 10, 20);
        rwrw.start();
        System.out.println(rwrw.getConclusion());

        System.out.println("===================StampedLock=================");
        StampedRW rw = new StampedRW(1000 * 1000 * 10, 10, 20);
        rw.start();
        System.out.println(rw.getConclusion());

        System.out.println("===================Synchronized=================");
        SynchronizedRW srw = new SynchronizedRW(1000 * 1000 * 10, 10, 20);
        srw.start();
        System.out.println(srw.getConclusion());
    }

}
