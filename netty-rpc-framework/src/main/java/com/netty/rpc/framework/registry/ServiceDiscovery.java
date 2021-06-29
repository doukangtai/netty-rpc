package com.netty.rpc.framework.registry;

import com.netty.rpc.common.extension.SPI;
import com.netty.rpc.framework.remoting.dto.RpcRequest;

import java.net.InetSocketAddress;

/**
 * @author 窦康泰
 * @date 2021/06/29
 */
@SPI
public interface ServiceDiscovery {
    InetSocketAddress lookupService(RpcRequest rpcRequest);
}
