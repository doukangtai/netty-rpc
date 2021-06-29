package com.netty.rpc.framework.registry.zk;

import com.netty.rpc.framework.registry.ServiceRegistry;
import com.netty.rpc.framework.registry.zk.util.CuratorUtils;

import java.net.InetSocketAddress;

/**
 * @author 窦康泰
 * @date 2021/06/29
 */
public class ZkServiceRegistryImpl implements ServiceRegistry {
    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        String path = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName + inetSocketAddress.toString();
        CuratorUtils.createPersistentNode(CuratorUtils.getZkClient(), path);
    }
}
