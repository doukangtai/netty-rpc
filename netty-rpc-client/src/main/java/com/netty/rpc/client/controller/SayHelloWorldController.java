package com.netty.rpc.client.controller;

import com.netty.rpc.api.service.SayHelloWorldService;
import com.netty.rpc.framework.annotation.NettyRpcReference;
import org.springframework.stereotype.Controller;

/**
 * @author 窦康泰
 * @date 2021/07/01
 */
@Controller
public class SayHelloWorldController {
    @NettyRpcReference(version = "version1", group = "group1")
    SayHelloWorldService sayHelloWorldService;

    public String sayHello(String msg) {
        return sayHelloWorldService.say(msg);
    }
}
