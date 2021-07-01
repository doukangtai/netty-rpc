package com.netty.rpc.framework.proxy;

import com.netty.rpc.common.enums.RpcErrorMessageEnum;
import com.netty.rpc.common.enums.RpcResponseCodeEnum;
import com.netty.rpc.common.exception.RpcException;
import com.netty.rpc.common.factory.SingletonFactory;
import com.netty.rpc.framework.config.RpcServiceConfig;
import com.netty.rpc.framework.remoting.dto.RpcRequest;
import com.netty.rpc.framework.remoting.dto.RpcResponse;
import com.netty.rpc.framework.remoting.transport.netty.client.NettyRpcClient;

import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author 窦康泰
 * @date 2021/07/01
 */
public class RpcClientProxy {
    private final RpcServiceConfig rpcServiceConfig;
    private final NettyRpcClient nettyRpcClient = SingletonFactory.getInstance(NettyRpcClient.class);

    public RpcClientProxy(RpcServiceConfig rpcServiceConfig) {
        this.rpcServiceConfig = rpcServiceConfig;
    }

    public <T> Object getProxy(Class<T> clazz) {
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, (proxy, method, args) -> {
            RpcRequest rpcRequest = new RpcRequest(
                    UUID.randomUUID().toString(),
                    method.getDeclaringClass().getName(),
                    method.getName(),
                    args,
                    method.getParameterTypes(),
                    rpcServiceConfig.getVersion(),
                    rpcServiceConfig.getGroup()
            );
            CompletableFuture<RpcResponse<Object>> completableFuture = nettyRpcClient.sendRpcRequest(rpcRequest);
            RpcResponse<Object> rpcResponse = completableFuture.get();
            check(rpcRequest, rpcResponse);
            return rpcResponse.getData();
        });
    }

    private void check(RpcRequest rpcRequest, RpcResponse<Object> rpcResponse) {
        if (rpcResponse == null || RpcResponseCodeEnum.SUCCESS.getCode() != rpcResponse.getCode()) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, rpcRequest.getInterfaceName());
        }
        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
            throw new RpcException(RpcErrorMessageEnum.REQUEST_NOT_MATCH_RESPONSE, rpcRequest.getInterfaceName());
        }
    }
}
