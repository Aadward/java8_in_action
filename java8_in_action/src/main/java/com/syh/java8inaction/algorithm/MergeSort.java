package com.syh.java8inaction.algorithm;

public class MergeSort {

    private static int[] aux;

    public static void main(String[] args) {
        System.out.println(SortVerifier.verify(MergeSort::mergeSort));
    }

    public static void mergeSort(int[] a) {
        if (aux == null || aux.length < a.length) {
            aux = new int[a.length];
        }
        sort(a, 0, a.length - 1);
    }

    public static void sort(int[] a, int lo, int hi) {
        if (lo >= hi) {
            return;
        }

        int mid = lo + (hi - lo) / 2;
        sort(a, lo, mid);
        //这里用 mid + 1 而不是在上面用mid - 1，
        //比如当lo = 0, hi = 1的时候，如果使用mid - 1, 则切分为[0,-1]和[0,1]，导致无限循环
        sort(a, mid + 1, hi);
        merge(a, lo, mid, hi);
    }

    /**
     * 合并两个有序数组[lo, mid-1] 和 [mid, hi]
     */
    public static void merge(int[] a, int lo, int mid, int hi) {
        for (int i = lo; i <= hi; i++) {
            aux[i] = a[i];
        }

        int i = lo, j = mid + 1, pointer = lo;
        while (pointer <= hi) {
            if (i > mid) {
                a[pointer++] = aux[j++];
            } else if (j > hi) {
                a[pointer++] = aux[i++];
            } else if (aux[i] < aux[j]) {
                a[pointer++] = aux[i++];
            } else {
                a[pointer++] = aux[j++];
            }
        }
    }
}
