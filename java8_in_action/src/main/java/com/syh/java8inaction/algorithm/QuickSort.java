package com.syh.java8inaction.algorithm;

import java.util.Random;

public class QuickSort {

    public static void main(String[] args) {
        System.out.println(SortVerifier.verify(QuickSort::quickSort));
    }

    public static void quickSort(int[] a) {
        sort(a, 0, a.length - 1);
    }

    private static void sort(int[] a, int lo, int hi) {
        if (lo < hi) {
            int mid = partition(a, lo, hi);
            sort(a, lo, mid - 1);
            sort(a, mid + 1, hi);
        }
    }

    //注意先移动再比较
    private static int partition(int[] a, int lo, int hi) {
        //随机找出一个数字
        int seedIndex = new Random().nextInt(hi - lo) + lo;
        swap(a, lo, seedIndex);

        int seed = a[lo];
        int i = lo, j = hi + 1;
        while (i < j) {
            while (i < j && i < --j && a[j] > seed);
            while (i < j && ++i < j && a[i] < seed);
            if (i < j) {
                swap(a, i, j);
            }
        }
        swap(a, lo, i);
        return i;
    }


    private static void swap(int[] a, int index1, int index2) {
        int tmp = a[index1];
        a[index1] = a[index2];
        a[index2] = tmp;
    }
}
