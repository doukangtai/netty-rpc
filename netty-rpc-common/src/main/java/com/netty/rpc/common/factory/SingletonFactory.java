package com.netty.rpc.common.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 窦康泰
 * @date 2021/06/29
 */
public class SingletonFactory {
    private static final Map<String, Object> STRING_OBJECT_MAP = new ConcurrentHashMap<>();

    public static <T> T getInstance(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalStateException();
        }
        String key = clazz.toString();
        return clazz.cast(STRING_OBJECT_MAP.computeIfAbsent(key, s -> {
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }));
    }
}
