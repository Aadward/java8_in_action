package com.syh.java8inaction.lock.readwrite;


public class ReadWriteTest {

    public static void main(String[] args) {

        System.out.println("===================ReadWriteLock=================");
        ReadWriteRW rwrw = new ReadWriteRW();
        rwrw.start();
        System.out.println(rwrw.getConclusion());

        System.out.println("===================StampedLock=================");
        StampedRW rw = new StampedRW();
        rw.start();
        System.out.println(rw.getConclusion());

        System.out.println("===================Synchronized=================");
        SynchronizedRW srw = new SynchronizedRW();
        srw.start();
        System.out.println(srw.getConclusion());
    }

}
