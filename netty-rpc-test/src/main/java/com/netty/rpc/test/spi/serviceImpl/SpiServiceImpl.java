package com.netty.rpc.test.spi.serviceImpl;

import com.netty.rpc.api.spi.SpiService;

/**
 * @author 窦康泰
 * @date 2021/06/27
 */
public class SpiServiceImpl implements SpiService {
    @Override
    public void say(String msg) {
        System.out.println("SpiServiceImpl-----say: " + msg);
    }
}
