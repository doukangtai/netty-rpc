package com.netty.rpc.framework.registry;

import com.netty.rpc.common.extension.SPI;

import java.net.InetSocketAddress;

/**
 * @author 窦康泰
 * @date 2021/06/29
 */
@SPI
public interface ServiceRegistry {
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);
}
