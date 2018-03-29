package com.syh.java8inaction.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FindPrime {

    public static void main(String[] args) {
        System.out.println(findByForkJoinPool(1, 100));
    }

    public static List<Integer> findByForkJoinPool(int lo, int hi) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        return pool.invoke(new PrimeNumberTask(lo, hi));
    }

    static class PrimeNumberTask extends RecursiveTask<List<Integer>> {

        static final int THRESHOLD = 10;

        private int lo;
        private int hi;

        public PrimeNumberTask(int lo, int hi) {
            this.lo = lo;
            this.hi = hi;
        }

        @Override
        protected List<Integer> compute() {
            if (hi - lo < THRESHOLD) {
                return getPrimeNumbers(lo, hi);
            } else {
                return invokeAll(createSubTask(lo, hi))
                        .stream()
                        .map(ForkJoinTask::join)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
            }
        }

        private List<PrimeNumberTask> createSubTask(int lo, int hi) {
            List<PrimeNumberTask> ret = new ArrayList<>();
            int mid = lo + (hi - lo) / 2;
            ret.add(new PrimeNumberTask(lo, mid));
            ret.add(new PrimeNumberTask(mid + 1, hi));
            return ret;
        }
    }


    private static List<Integer> getPrimeNumbers(int lo, int hi) {
        return IntStream.range(lo, hi)
                .filter(FindPrime::isPrime)
                .boxed()
                .collect(Collectors.toList());
    }

    private static boolean isPrime(int number) {
        int hi = (int) Math.floor(Math.sqrt(number));

        for (int i = 2; i <= hi; i++) {
            if (number %i == 0) {
                return false;
            }
        }
        return true;
    }

}
