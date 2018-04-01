package com.syh.java8inaction.algorithm;

import java.util.Arrays;
import java.util.Random;

public class MedianFinder {

    public static void main(String[] args) {
        int[] a = SortVerifier.getRamdomArray(1);
        int[] b = Arrays.copyOf(a, a.length);

        Arrays.sort(a);
        int expect = a.length % 2 == 0 ? ((a[a.length / 2 - 1]+ a[a.length / 2])) / 2 : a[a.length / 2];
        int actual = findMedian(b);
        System.out.println("expect=" + expect + ", actual=" + actual + ". equals=" + (expect == actual));
    }

    static Random random = new Random(443);

    public static int findMedian(int[] a) {
        int lo = 0, hi = a.length - 1;
        int mid, target = a.length / 2;
        while ((mid = partition(a, lo, hi)) != target) {
            if (mid > target) {
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        if (a.length % 2 == 0) {
            int max = findMax(a, 0, mid - 1);
            return (a[mid] + max) / 2;
        } else {
            return a[mid];
        }
    }

    public static int findMax(int[] a, int lo, int hi) {
        int max = a[lo];
        for (int i = lo + 1; i <= hi; i++) {
            if (a[i] > max) {
                max = a[i];
            }
        }
        return max;
    }

    static int partition(int[] a, int lo, int hi) {
        if (lo == hi) {
            return lo;
        }
        int seedIndex = random.nextInt(hi - lo) + lo;
        swap(a, lo, seedIndex);

        int seed = a[lo];
        int i = lo, j = hi + 1;
        while (true) {
            while (a[--j] > seed && i < j);
            while (a[++i] < seed && i < j);
            if (i < j) {
                swap(a, i, j);
            } else {
                break;
            }
        }
        swap(a, lo, i);
        return i;
    }

    public static void swap(int[] a, int index1, int index2) {
        int tmp = a[index1];
        a[index1] = a[index2];
        a[index2] = tmp;
    }
}
