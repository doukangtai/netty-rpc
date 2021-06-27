package com.netty.rpc.common.extension;

/**
 * @author 窦康泰
 * @date 2021/06/27
 */
public class Holder<T> {
    private volatile T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}
