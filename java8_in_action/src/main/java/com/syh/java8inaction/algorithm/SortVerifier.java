package com.syh.java8inaction.algorithm;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SortVerifier {

    static final int TEST_SIZE = 1000000;

    static Random random = new Random(333);

    static boolean verify(Consumer<int[]> sort) {
        int[] a = getRamdomArray(TEST_SIZE);
        int[] b = Arrays.copyOf(a, a.length);
        Arrays.sort(a);
        sort.accept(b);
        boolean ok = Arrays.equals(a, b);
        if (!ok) {
            System.out.println("expect: " + print(a));
            System.out.println("actual: " + print(b));
        }
        return ok;
    }

    static int[] getRamdomArray(int size) {
        return IntStream
                .generate(() -> generator(Integer.MAX_VALUE))
                .limit(size)
                .toArray();
    }

    static int[] getRamdomArray(int size, int max) {
        return IntStream
                .generate(() -> generator(max))
                .limit(size)
                .toArray();
    }

    static int generator(int max) {
        return random.nextInt(max);
    }

    static String print(int[] a) {
        return Arrays.stream(a)
                        .boxed()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));

    }
}
