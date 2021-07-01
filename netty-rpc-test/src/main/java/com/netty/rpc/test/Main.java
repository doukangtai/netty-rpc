package com.netty.rpc.test;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author 窦康泰
 * @date 2021/06/28
 */
public class Main {
    public static void main(String[] args) {
        MyClass myClass = new MyClass("Zhang San");
        MyInterface myInterface = (MyInterface) Proxy.newProxyInstance(MyClass.class.getClassLoader(), MyClass.class.getInterfaces(), (proxy, method, args1) -> {
            Class<?> declaringClass = method.getDeclaringClass();
            System.out.println(declaringClass);
            System.out.println(method.getClass());
            for (Method declaredMethod : declaringClass.getDeclaredMethods()) {
                System.out.println(declaredMethod.getName());
            }
//            method.invoke(myClass, args1);
            return null;
        });
        myInterface.sayName();
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
