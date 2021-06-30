package com.netty.rpc.framework.remoting.handler;

import com.netty.rpc.common.enums.RpcErrorMessageEnum;
import com.netty.rpc.common.exception.RpcException;
import com.netty.rpc.common.factory.SingletonFactory;
import com.netty.rpc.framework.provider.ServiceProvider;
import com.netty.rpc.framework.provider.impl.ZkServiceProviderImpl;
import com.netty.rpc.framework.remoting.dto.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author 窦康泰
 * @date 2021/06/30
 */
public class RpcRequestHandler {
    private static final Logger log = LoggerFactory.getLogger(RpcRequestHandler.class);
    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);

    public Object handle(RpcRequest rpcRequest) {
        Object service = serviceProvider.getService(rpcRequest.getRpcServiceName());
        return invokeTargetMethod(rpcRequest, service);
    }

    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        try {
            Method declaredMethod = service.getClass().getDeclaredMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            declaredMethod.setAccessible(true);
            Object result = declaredMethod.invoke(service, rpcRequest.getParameters());
            log.info("service:[{}] successful invoke method:[{}], get result:[{}]", rpcRequest.getInterfaceName(), rpcRequest.getMethodName(), result.toString());
            return result;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, e.getMessage());
        }
    }
}
