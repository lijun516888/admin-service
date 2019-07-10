package com.knowledge.example;

import org.junit.Test;

public class ThreadLocalTest {

    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    @Test
    public void threadLocal1() {
        new Thread(() -> {
            threadLocal.set(Thread.currentThread().getId());
            System.out.println(threadLocal.get());
        }).start();
        new Thread(() -> {
            threadLocal.set(Thread.currentThread().getId());
            System.out.println(threadLocal.get());
        }).start();
        new Thread(() -> {
            threadLocal.set(Thread.currentThread().getId());
            System.out.println(threadLocal.get());
        }).start();
    }

}
