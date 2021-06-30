package com.netty.rpc.framework.provider.impl;

import com.netty.rpc.common.enums.RpcErrorMessageEnum;
import com.netty.rpc.common.exception.RpcException;
import com.netty.rpc.common.extension.ExtensionLoader;
import com.netty.rpc.framework.config.RpcServiceConfig;
import com.netty.rpc.framework.provider.ServiceProvider;
import com.netty.rpc.framework.registry.ServiceRegistry;
import com.netty.rpc.framework.remoting.transport.netty.server.NettyRpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 窦康泰
 * @date 2021/06/29
 */
public class ZkServiceProviderImpl implements ServiceProvider {
    private static final Logger log = LoggerFactory.getLogger(ZkServiceProviderImpl.class);
    private final Map<String, Object> serviceMap;
    private final ServiceRegistry serviceRegistry;

    public ZkServiceProviderImpl() {
        serviceMap = new ConcurrentHashMap<>();
        serviceRegistry = ExtensionLoader.getExtensionLoader(ServiceRegistry.class).getExtension("zk");
    }

    @Override
    public void addService(RpcServiceConfig rpcServiceConfig) {
        String rpcServiceName = rpcServiceConfig.getRpcServiceName();
        if (serviceMap.containsKey(rpcServiceName)) {
            return;
        }
        serviceMap.put(rpcServiceName, rpcServiceConfig.getService());
    }

    @Override
    public Object getService(String rpcServiceName) {
        if (serviceMap.containsKey(rpcServiceName)) {
            return serviceMap.get(rpcServiceName);
        }
        throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
    }

    @Override
    public void publishService(RpcServiceConfig rpcServiceConfig) {
        addService(rpcServiceConfig);
        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("occur exception when getHostAddress:" + e.getMessage());
        }
        InetSocketAddress inetSocketAddress = new InetSocketAddress(host, NettyRpcServer.PORT);
        serviceRegistry.registerService(rpcServiceConfig.getRpcServiceName(), inetSocketAddress);
    }
}
