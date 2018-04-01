package com.syh.java8inaction.algorithm;

public class InsertSort {

    public static void main(String[] args) {
        System.out.println(SortVerifier.verify(InsertSort::sort));
    }

    static int[] sort(int[] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = i; j > 0; j--) {
                if (a[j] < a[j-1]) {
                    int tmp = a[j];
                    a[j] = a[j-1];
                    a[j-1] = tmp;
                } else {
                    break;
                }
            }
        }
        return a;
    }

    //可以减少赋值次数
    static int[] fastSort(int[] a) {
        for (int i = 0; i < a.length; i++) {
            int newest = a[i];
            for (int j = i; j > 0; j--) {
                if (newest < a[j-1]) {
                    a[j] = a[j-1];
                } else {
                    a[j] = newest;
                    break;
                }
            }
        }
        return a;
    }
}
