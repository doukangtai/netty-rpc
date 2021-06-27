package com.netty.rpc.test.scanner.controller;

import com.netty.rpc.framework.annotation.NettyRpcReference;
import com.netty.rpc.test.scanner.serviceImpl.SayHelloWorldServiceImpl;

/**
 * @author 窦康泰
 * @date 2021/06/27
 */
public class HelloWorldController {
    @NettyRpcReference
    private SayHelloWorldServiceImpl sayHelloWorldService;

    public void test() {
        sayHelloWorldService.say("调用成功");
    }
}
