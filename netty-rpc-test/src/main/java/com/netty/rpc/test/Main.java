package com.netty.rpc.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 窦康泰
 * @date 2021/06/28
 */
public class Main {
    private static final ThreadLocal<String> msgThreadLocal = ThreadLocal.withInitial(() -> "msg");

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 5; i++) {
            threadPool.execute(() -> {
                String msg = msgThreadLocal.get();
                String name = Thread.currentThread().getName();
                System.out.println(name + " : " + msg);
                msgThreadLocal.remove();
            });
        }
        threadPool.shutdown();
    }
}

class MyClass implements MyInterface {}

interface MyInterface {}
