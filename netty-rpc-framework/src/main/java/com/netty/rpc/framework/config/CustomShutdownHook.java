package com.netty.rpc.framework.config;

/**
 * @author 窦康泰
 * @date 2021/06/29
 */
public class CustomShutdownHook {
    public static void clearAll() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // clear
        }));
    }
}
