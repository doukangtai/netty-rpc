package com.netty.rpc.framework.serialize;

/**
 * @author 窦康泰
 * @date 2021/06/29
 */
public interface Serializer {
    byte[] serialize(Object obj);

    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
