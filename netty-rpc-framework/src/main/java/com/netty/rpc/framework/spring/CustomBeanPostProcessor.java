package com.netty.rpc.framework.spring;

import com.netty.rpc.common.factory.SingletonFactory;
import com.netty.rpc.framework.annotation.NettyRpcReference;
import com.netty.rpc.framework.annotation.NettyRpcService;
import com.netty.rpc.framework.config.RpcServiceConfig;
import com.netty.rpc.framework.provider.ServiceProvider;
import com.netty.rpc.framework.provider.impl.ZkServiceProviderImpl;
import com.netty.rpc.framework.proxy.RpcClientProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * 在Bean加载过程中进行注册服务或服务引用
 *
 * @author 窦康泰
 * @date 2021/06/27
 */
@Component
public class CustomBeanPostProcessor implements BeanPostProcessor {
    private static final Logger log = LoggerFactory.getLogger(CustomBeanPostProcessor.class);
    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 向zookeeper注册服务
        if (bean.getClass().isAnnotationPresent(NettyRpcService.class)) {
            NettyRpcService nettyRpcService = bean.getClass().getAnnotation(NettyRpcService.class);
            RpcServiceConfig rpcServiceConfig = new RpcServiceConfig(nettyRpcService.version(), nettyRpcService.group(), bean);
            serviceProvider.publishService(rpcServiceConfig);
        }
        // 服务引用
        for (Field declaredField : bean.getClass().getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(NettyRpcReference.class)) {
                NettyRpcReference nettyRpcReference = declaredField.getAnnotation(NettyRpcReference.class);
                String version = nettyRpcReference.version();
                String group = nettyRpcReference.group();
                RpcClientProxy rpcClientProxy = new RpcClientProxy(new RpcServiceConfig(version, group));
                Object proxy = rpcClientProxy.getProxy(declaredField.getType());
                declaredField.setAccessible(true);
                try {
                    declaredField.set(bean, proxy);
                } catch (IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return bean;
    }
}
