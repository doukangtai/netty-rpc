package com.netty.rpc.framework.remoting.transport.netty.client;

import com.netty.rpc.framework.remoting.dto.RpcResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 窦康泰
 * @date 2021/07/01
 */
public class UnprocessedRequests {
    private static final Map<String, CompletableFuture<RpcResponse<Object>>> FUTURE_MAP = new ConcurrentHashMap<>();

    public void put(String requireId, CompletableFuture<RpcResponse<Object>> completableFuture) {
        FUTURE_MAP.put(requireId, completableFuture);
    }

    public void complete(RpcResponse<Object> rpcResponse) {
        CompletableFuture<RpcResponse<Object>> rpcResponseCompletableFuture = FUTURE_MAP.remove(rpcResponse.getRequestId());
        if (rpcResponseCompletableFuture != null) {
            rpcResponseCompletableFuture.complete(rpcResponse);
        } else {
            throw new RuntimeException("processed requests exception");
        }
    }
}
