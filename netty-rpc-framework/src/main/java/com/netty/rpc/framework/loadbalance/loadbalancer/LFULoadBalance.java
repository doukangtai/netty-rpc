package com.netty.rpc.framework.loadbalance.loadbalancer;

import com.netty.rpc.framework.loadbalance.AbstractLoadBalance;
import com.netty.rpc.framework.remoting.dto.RpcRequest;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 窦康泰
 * @date 2021/07/02
 */
public class LFULoadBalance extends AbstractLoadBalance {
    private static final Map<String, HashMap<String, Integer>> LFU_CACHE_MAP = new ConcurrentHashMap<>();
    private static long CACHE_VALID_TIME = 0;

    @Override
    protected String doSelect(List<String> serviceAddresses, RpcRequest rpcRequest) {
        if (System.currentTimeMillis() > CACHE_VALID_TIME) {
            LFU_CACHE_MAP.clear();
            CACHE_VALID_TIME = System.currentTimeMillis() + 1000 * 60 * 60 * 24;
        }
        String rpcServiceName = rpcRequest.getRpcServiceName();
        HashMap<String, Integer> lfu = LFU_CACHE_MAP.get(rpcServiceName);
        if (lfu == null) {
            lfu = new HashMap<>();
            LFU_CACHE_MAP.put(rpcServiceName, lfu);
        }
        for (String serviceAddress : serviceAddresses) {
            if (!lfu.containsKey(serviceAddress) || lfu.get(serviceAddress) > 1000000) {
                lfu.put(serviceAddress, 0);
            }
        }
        List<String> deleteKeys = new ArrayList<>();
        for (String key : lfu.keySet()) {
            if (!serviceAddresses.contains(key)) {
                deleteKeys.add(key);
            }
        }
        for (String deleteKey : deleteKeys) {
            lfu.remove(deleteKey);
        }
        ArrayList<Map.Entry<String, Integer>> lfuList = new ArrayList<>(lfu.entrySet());
        Collections.sort(lfuList, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));
        String result = null;
        if (lfuList.size() > 0) {
            Map.Entry<String, Integer> entry = lfuList.get(0);
            result = entry.getKey();
            entry.setValue(entry.getValue() + 1);
        }
        return result;
    }
}
