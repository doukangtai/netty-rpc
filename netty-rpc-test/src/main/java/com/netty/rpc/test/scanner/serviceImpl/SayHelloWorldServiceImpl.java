package com.netty.rpc.test.scanner.serviceImpl;

import com.netty.rpc.api.service.SayHelloWorldService;
import com.netty.rpc.framework.annotation.NettyRpcService;

/**
 * @author 窦康泰
 * @date 2021/06/27
 */
@NettyRpcService
public class SayHelloWorldServiceImpl implements SayHelloWorldService {
    @Override
    public void say(String msg) {
        System.out.println("SayHelloWorldServiceImpl --- Class --- say --- method --- invoke");
        System.out.println("say: " + msg);
    }
}
