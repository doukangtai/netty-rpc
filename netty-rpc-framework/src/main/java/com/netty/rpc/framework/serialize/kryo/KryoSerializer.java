package com.netty.rpc.framework.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.netty.rpc.common.exception.SerializeException;
import com.netty.rpc.framework.serialize.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author 窦康泰
 * @date 2021/06/30
 */
public class KryoSerializer implements Serializer {
    @Override
    public <T> byte[] serialize(T obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {
            Kryo kryo = new Kryo();
            kryo.register(obj.getClass());
            kryo.writeObject(output, obj);
            // KryoException: Buffer underflow.
            // byte[] bytes = byteArrayOutputStream.toByteArray();
            return output.toBytes();
        } catch (IOException e) {
            throw new SerializeException("serialize fail");
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = new Kryo();
            kryo.register(clazz);
            T object = kryo.readObject(input, clazz);
            return object;
        } catch (IOException e) {
            throw new SerializeException("deserialize fail");
        }
    }
}
