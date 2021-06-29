package com.netty.rpc.framework.loadbalance.loadbalancer;

import com.netty.rpc.framework.loadbalance.AbstractLoadBalance;
import com.netty.rpc.framework.remoting.dto.RpcRequest;

import java.util.List;
import java.util.Random;

/**
 * @author 窦康泰
 * @date 2021/06/29
 */
public class RandomLoadBalance extends AbstractLoadBalance {
    @Override
    protected String doSelect(List<String> serviceAddresses, RpcRequest rpcRequest) {
        return serviceAddresses.get(new Random().nextInt(serviceAddresses.size()));
    }
}
