package com.syh.java8inaction.algorithm;

import java.util.Arrays;
import java.util.Iterator;

public class BitMap implements Iterable<Integer> {

    public static void main(String[] args) {

        int maxValue = 10000000;

        int[] a = Arrays.stream(SortVerifier.getRamdomArray(100000, maxValue))
                .distinct()
                .toArray();
        int[] b = Arrays.copyOf(a, a.length);

        //sort b by this class
        BitMap bitMap = new BitMap(maxValue);
        bitMap.addAll(b);
        int[] actual = bitMap.getSorted();

        //sort a by jdk api
        Arrays.sort(a);

        //check validity
        System.out.println(Arrays.equals(a, actual));
    }

    private byte[] content;
    private int size;

    public BitMap(int range) {
        int i = 1;
        while (i < range) {
            i = i << 1;
        }
        int nByte = i / 8;
        content = new byte[nByte];
        size = 0;
    }

    public void addAll(int[] a) {
        Arrays.stream(a).forEach(this::add);
    }

    public void add(int a) {
        int locate = a / 8;
        int bitOffset = 7 - (a % 8);
        content[locate] |= (1 << bitOffset);
        size += 1;
    }

    private boolean exist(int a) {
        int locate = a / 8;
        int bitOffset = 7 - (a % 8);
        return (content[locate] & (1 <<bitOffset)) != 0;
    }


    public int[] getSorted() {
        int[] ret = new int[size];
        int i = 0;
        for (Integer item : this) {
            ret[i++] = item;
        }
        return ret;
    }


    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {

            int hasReadCount = 0;
            int index = 0;

            @Override
            public boolean hasNext() {
                return hasReadCount < size;
            }

            @Override
            public Integer next() {
                while (!exist(index)) {
                    index += 1;
                }
                int ret = index;
                hasReadCount += 1;
                index += 1;
                return ret;
            }
        };
    }
}
