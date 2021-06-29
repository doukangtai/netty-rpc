package com.netty.rpc.framework.registry.zk;

import com.netty.rpc.common.enums.RpcErrorMessageEnum;
import com.netty.rpc.common.exception.RpcException;
import com.netty.rpc.common.extension.ExtensionLoader;
import com.netty.rpc.framework.loadbalance.LoadBalance;
import com.netty.rpc.framework.registry.ServiceDiscovery;
import com.netty.rpc.framework.registry.zk.util.CuratorUtils;
import com.netty.rpc.framework.remoting.dto.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author 窦康泰
 * @date 2021/06/29
 */
public class ZkServiceDiscoveryImpl implements ServiceDiscovery {
    private static final Logger log = LoggerFactory.getLogger(ZkServiceDiscoveryImpl.class);
    private final LoadBalance loadBalance;

    public ZkServiceDiscoveryImpl() {
        this.loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension("loadBalance");
    }

    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        List<String> childrenNodes = CuratorUtils.getChildrenNodes(CuratorUtils.getZkClient(), rpcRequest.getRpcServiceName());
        if (childrenNodes == null || childrenNodes.size() == 0) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND, rpcRequest.getRpcServiceName());
        }
        String serviceAddress = loadBalance.selectServiceAddress(childrenNodes, rpcRequest);
        log.info("Successfully found the service address:[{}]", serviceAddress);
        String[] hostPort = serviceAddress.split(":");
        String host = hostPort[0];
        int port = Integer.parseInt(hostPort[1]);
        return new InetSocketAddress(host, port);
    }
}
