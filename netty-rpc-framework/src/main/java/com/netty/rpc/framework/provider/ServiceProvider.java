package com.netty.rpc.framework.provider;

import com.netty.rpc.framework.config.RpcServiceConfig;

/**
 * @author 窦康泰
 * @date 2021/06/29
 */
public interface ServiceProvider {
    void addService(RpcServiceConfig rpcServiceConfig);

    Object getService(String rpcServiceName);

    void publishService(RpcServiceConfig rpcServiceConfig);
}
