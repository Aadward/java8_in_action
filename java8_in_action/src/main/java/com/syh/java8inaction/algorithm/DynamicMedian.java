package com.syh.java8inaction.algorithm;

import java.util.Arrays;
import java.util.PriorityQueue;

public class DynamicMedian {

    public static void main(String[] args) {
        int[] a = SortVerifier.getRamdomArray(1000);
        int[] b = Arrays.copyOf(a, a.length);

        Arrays.sort(a);
        double expect = a.length % 2 == 0 ? ((double)(a[a.length / 2 - 1]+ a[a.length / 2])) / 2 : a[a.length / 2];
        double actual = new DynamicMedian(b).findMedian();
        System.out.println("expect=" + expect + ", actual=" + actual + ". equals=" + (expect == actual));
    }

    private PriorityQueue<Integer> small = new PriorityQueue<>((o1, o2) -> {
        if (o1 > o2) {
            return -1;
        } else if (o1 < o2) {
            return 1;
        } else {
            return 0;
        }
    });

    private PriorityQueue<Integer> big = new PriorityQueue<>();

    public DynamicMedian(int[] a) {
        Arrays.stream(a).forEach(this::add);
    }

    public void add(int n) {
        if (small.isEmpty()) {
            small.add(n);
            return;
        }

        if (n > small.peek()) {
            if (big.size() - small.size() == 1) {
                small.add(big.poll());
            }
            big.add(n);
        } else {
            if (small.size() - big.size() == 1) {
                big.add(small.poll());
            }
            small.add(n);
        }
    }

    public double findMedian() {
        if (big.size() == small.size()) {
            return ((double) (big.peek() + small.peek())) / 2;
        } else if (big.size() > small.size()) {
            return big.peek();
        } else {
            return small.peek();
        }
    }
}
