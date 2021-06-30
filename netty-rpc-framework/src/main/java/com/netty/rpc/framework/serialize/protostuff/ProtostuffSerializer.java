package com.netty.rpc.framework.serialize.protostuff;

import com.netty.rpc.framework.serialize.Serializer;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 窦康泰
 * @date 2021/06/29
 */
public class ProtostuffSerializer implements Serializer {
    private static final Map<Class<?>, Schema<?>> SCHEMA_MAP = new ConcurrentHashMap<>();

    @Override
    public <T> byte[] serialize(T obj) {
        Class<T> clazz = (Class<T>) obj.getClass();
        Schema<T> schema = getSchema(clazz);
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        byte[] bytes = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        buffer.clear();
        return bytes;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Schema<T> schema = getSchema(clazz);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        return obj;
    }

    public <T> Schema<T> getSchema(Class<T> clazz) {
        if (SCHEMA_MAP.containsKey(clazz)) {
            return (Schema<T>) SCHEMA_MAP.get(clazz);
        }
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        if (schema != null) {
            SCHEMA_MAP.put(clazz, schema);
        }
        return (Schema<T>) SCHEMA_MAP.get(clazz);
    }
}
