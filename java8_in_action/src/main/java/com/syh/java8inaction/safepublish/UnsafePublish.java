package com.syh.java8inaction.safepublish;


/**
 * 不安全的发布，因为构造函数不是原子操作，可能将未构造完全的Holder类发布。
 *
 * 但是在本地机器上始终无法复现，不确定是JIT优化还是其他原因，还是JVM优化
 * 了内存模型。
 *
 * 有可能在多线程环境下执行的类都需要安全发布。
 */
public class UnsafePublish {

    Holder holder;

    public static void main(String[] args) {
        UnsafePublish unsafe = new UnsafePublish();
        new Thread(() -> unsafe.holder = new Holder(5)).start();
        new Thread(() -> {
            if (unsafe.holder != null) {
                unsafe.holder.assertSanity();
            }
        }).start();
    }

    static class Holder {
        private int n;

        public Holder(int n ) {this.n = n;}

        public void assertSanity() {
            if(n != n)
                throw new AssertionError("This statement is false.");
        }
    }
}
