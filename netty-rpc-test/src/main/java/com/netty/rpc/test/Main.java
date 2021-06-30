package com.netty.rpc.test;

import com.netty.rpc.framework.compress.gzip.GzipCompress;
import com.netty.rpc.framework.serialize.Serializer;
import com.netty.rpc.framework.serialize.kryo.KryoSerializer;

/**
 * @author 窦康泰
 * @date 2021/06/28
 */
public class Main {
    public static void main(String[] args) {
        Serializer serializer = new KryoSerializer();
        byte[] bytes = serializer.serialize(new MyClass("AAA"));
        GzipCompress gzipCompress = new GzipCompress();
        byte[] compress = gzipCompress.compress(bytes);
        byte[] decompress = gzipCompress.decompress(compress);
        MyClass myClass = serializer.deserialize(decompress, MyClass.class);
        System.out.println(myClass);
    }
}

class MyClass implements MyInterface {
    private String name;

    public MyClass() {
    }

    public MyClass(String name) {
        this.name = name;
    }

    @Override
    public void sayName() {
        System.out.println("say:" + name);
    }

    @Override
    public String toString() {
        return "MyClass{" +
                "name='" + name + '\'' +
                '}';
    }
}

interface MyInterface {
    void sayName();
}
