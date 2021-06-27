package com.netty.rpc.framework.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 在Bean加载过程中进行注册服务或服务引用
 *
 * @author 窦康泰
 * @date 2021/06/27
 */
@Component
public class CustomBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 向zookeeper注册服务
        // code
        // 服务引用
        // code
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
