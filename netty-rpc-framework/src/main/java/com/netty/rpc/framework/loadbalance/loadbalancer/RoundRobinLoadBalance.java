package com.netty.rpc.framework.loadbalance.loadbalancer;

import com.netty.rpc.framework.loadbalance.AbstractLoadBalance;
import com.netty.rpc.framework.remoting.dto.RpcRequest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 窦康泰
 * @date 2021/07/02
 */
public class RoundRobinLoadBalance extends AbstractLoadBalance {
    private static final Map<String, AtomicInteger> ATOMIC_INTEGER_MAP = new ConcurrentHashMap<>();

    @Override
    protected String doSelect(List<String> serviceAddresses, RpcRequest rpcRequest) {
        String rpcServiceName = rpcRequest.getRpcServiceName();
        AtomicInteger atomicInteger;
        if (ATOMIC_INTEGER_MAP.containsKey(rpcServiceName)) {
            atomicInteger = ATOMIC_INTEGER_MAP.get(rpcServiceName);
        } else {
            ATOMIC_INTEGER_MAP.put(rpcServiceName, new AtomicInteger());
            atomicInteger = ATOMIC_INTEGER_MAP.get(rpcServiceName);
        }
        return serviceAddresses.get(atomicInteger.getAndIncrement() % serviceAddresses.size());
    }
}
