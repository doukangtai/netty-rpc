package com.netty.rpc.framework.serialize.protostuff;

import com.netty.rpc.framework.serialize.Serializer;

/**
 * @author 窦康泰
 * @date 2021/06/29
 */
public class ProtostuffSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return null;
    }
}
