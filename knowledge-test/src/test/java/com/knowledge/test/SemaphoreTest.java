package com.knowledge.test;

import java.util.concurrent.Semaphore;

public class SemaphoreTest {

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(0);
        for(int i = 0; i < 2; i ++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " starting get!");
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + " get ok!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            semaphore.release();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            semaphore.release();
        }).start();
    }

}
