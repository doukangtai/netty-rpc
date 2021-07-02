package com.netty.rpc.framework.loadbalance.loadbalancer;

import com.netty.rpc.framework.loadbalance.AbstractLoadBalance;
import com.netty.rpc.framework.remoting.dto.RpcRequest;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 窦康泰
 * @date 2021/07/02
 */
public class LRULoadBalance extends AbstractLoadBalance {
    private static final Map<String, LinkedHashMap<String, String>> LRU_CACHE_MAP = new ConcurrentHashMap<>();
    private static long CACHE_VALID_TIME = 0;

    @Override
    protected String doSelect(List<String> serviceAddresses, RpcRequest rpcRequest) {
        if (System.currentTimeMillis() > CACHE_VALID_TIME) {
            LRU_CACHE_MAP.clear();
            CACHE_VALID_TIME = System.currentTimeMillis() + 1000 * 60 * 60 * 24;
        }
        String rpcServiceName = rpcRequest.getRpcServiceName();
        LinkedHashMap<String, String> lru = LRU_CACHE_MAP.get(rpcServiceName);
        if (lru == null) {
            lru = new LinkedHashMap<String, String>(16, 0.75F, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry eldest) {
                    return super.size() > 100;
                }
            };
            LRU_CACHE_MAP.putIfAbsent(rpcServiceName, lru);
        }
        for (String serviceAddress : serviceAddresses) {
            if (!lru.containsKey(serviceAddress)) {
                lru.put(serviceAddress, serviceAddress);
            }
        }
        List<String> deleteKeys = new ArrayList<>();
        for (String key : lru.keySet()) {
            if (!serviceAddresses.contains(key)) {
                deleteKeys.add(key);
            }
        }
        for (String deleteKey : deleteKeys) {
            lru.remove(deleteKey);
        }
        Iterator<Map.Entry<String, String>> iterator = lru.entrySet().iterator();
        if (iterator.hasNext()) {
            String key = iterator.next().getKey();
            return lru.get(key);
        }
        return null;
    }
}
