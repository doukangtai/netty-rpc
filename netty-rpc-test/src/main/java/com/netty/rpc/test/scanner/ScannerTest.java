package com.netty.rpc.test.scanner;

import com.netty.rpc.framework.annotation.NettyRpcScan;
import com.netty.rpc.test.scanner.serviceImpl.SayHelloWorldServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author 窦康泰
 * @date 2021/06/27
 */
@NettyRpcScan(basePackages = {"com.netty.rpc.test.serviceImpl", "com.netty.rpc.test.controller", "com.netty.rpc.test", "com.dkt.demo"})
public class ScannerTest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ScannerTest.class);
        SayHelloWorldServiceImpl sayHelloWorldService = context.getBean(SayHelloWorldServiceImpl.class);
        sayHelloWorldService.say("调用成功");
    }
}
