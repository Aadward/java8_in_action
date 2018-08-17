package com.syh.java8inaction.algorithm;

public class BinarySearch {

    static final int[] a = {1,2,3,4,5,7,8,9,10,15,20,30};

    public static void main(String[] args) {
        System.out.println(binarySearch(8));
        System.out.println(binarySearch(1));
        System.out.println(binarySearch(30));
        System.out.println(binarySearch(14));
    }

    static int binarySearch(int val) {
        int lo = 0, hi = a.length - 1;
        int index;

        while (lo <= hi) {
            index = lo + (hi - lo) / 2;
            if (a[index] == val) {
                return index;
            } else if (a[index] > val) {
                hi = index - 1;
            } else {
                lo = index + 1;
            }
        }

        return -1;
    }
}
