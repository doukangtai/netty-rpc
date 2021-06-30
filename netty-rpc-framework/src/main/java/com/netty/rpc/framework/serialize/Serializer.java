package com.netty.rpc.framework.serialize;

import com.netty.rpc.common.extension.SPI;

/**
 * @author 窦康泰
 * @date 2021/06/29
 */
@SPI
public interface Serializer {
    <T> byte[] serialize(T obj);

    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
