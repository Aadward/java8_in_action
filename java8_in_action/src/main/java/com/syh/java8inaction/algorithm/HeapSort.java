package com.syh.java8inaction.algorithm;

import java.util.stream.IntStream;

public class HeapSort {

    public static void main(String[] args) {
        System.out.println(SortVerifier.verify(HeapSort::sort));
    }


    public static void sort(int[] a) {
        MinHeap heap = new MinHeap(a);
        //Arrays.stream(a).forEach(heap::add);
        int size = heap.n;
        int[] result = IntStream.generate(heap::delMin).limit(size).toArray();
        for (int i = 0; i < result.length; i++) {
            a[i] = result[i];
        }
    }


    private static class MinHeap {
        int a[];
        int n = 0;

        public MinHeap(int size) {
            a = new int[size + 1];
        }

        public MinHeap(int[] a) {
            int[] copy = new int[a.length + 1];
            System.arraycopy(a, 0, copy, 1, a.length);
            this.a = copy;
            this.n = copy.length - 1;
            for (int i = a.length / 2; i > 0; i--) {
                sink(i);
            }
        }

        void sink(int i) {
            while (i * 2 <= n) {
                int j = i * 2;
                if (j + 1 <= n) {
                    j = a[j] > a[j+1] ? j+1 : j;
                }
                if (a[i] > a[j]) {
                    swap(a, i, j);
                    i = j;
                } else {
                    break;
                }
            }
        }

        void swim(int i) {
            while (i / 2 >= 1) {
                if (a[i] < a[i / 2]) {
                    swap(a, i, i / 2);
                    i = i / 2;
                } else {
                    break;
                }
            }
        }

        void add(int i) {
            a[++n] = i;
            swim(n);
        }

        int delMin() {
            int min = a[1];
            a[1] = a[n--];
            sink(1);
            return min;
        }

        static void swap(int[] a, int index1, int index2) {
            int tmp = a[index1];
            a[index1] = a[index2];
            a[index2] = tmp;
        }
    }
}
