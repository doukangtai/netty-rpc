package com.netty.rpc.client;

import com.netty.rpc.client.controller.SayHelloWorldController;
import com.netty.rpc.framework.annotation.NettyRpcScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author 窦康泰
 * @date 2021/07/01
 */
@NettyRpcScan(basePackages = {"com.netty.rpc.client.controller"})
public class NettyRpcClientMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(NettyRpcClientMain.class);
        SayHelloWorldController sayHelloWorldController = context.getBean(SayHelloWorldController.class);
        sayHelloWorldController.sayHello("hhh");
        sayHelloWorldController.sayHello("hhh");
        sayHelloWorldController.sayHello("hhh");
        sayHelloWorldController.sayHello("hhh");
        String msg = sayHelloWorldController.sayHello("豌豆起飞了");
        System.out.println("收到：" + msg);
    }
}
