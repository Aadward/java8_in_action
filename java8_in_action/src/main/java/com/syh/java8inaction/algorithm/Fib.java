package com.syh.java8inaction.algorithm;

public class Fib {

    public static void main(String[] args) {
        System.out.println(slowFib(6));
        System.out.println(fastFib(6));
    }

    public static long slowFib(int n) {
        return (n == 1 || n ==2) ? 1 : slowFib(n - 1) + slowFib(n - 2);
    }

    public static long fastFib(int n) {
        return fib(n, 1, 1);
    }

    private static long fib(int n, int a1, int a2) {
        if (n <= 2) {
            return 1;
        } else if (n == 3) {
            return a1 + a2;
        }
        return fib(--n, a2, a1 + a2);
    }
}
