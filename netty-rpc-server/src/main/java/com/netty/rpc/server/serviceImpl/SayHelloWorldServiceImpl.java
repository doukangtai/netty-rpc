package com.netty.rpc.server.serviceImpl;

import com.netty.rpc.api.service.SayHelloWorldService;
import com.netty.rpc.framework.annotation.NettyRpcService;

/**
 * @author 窦康泰
 * @date 2021/07/01
 */
@NettyRpcService(version = "version1", group = "group1")
public class SayHelloWorldServiceImpl implements SayHelloWorldService {
    @Override
    public String say(String msg) {
        System.out.println("说的内容：" + msg);
        return msg;
    }
}
