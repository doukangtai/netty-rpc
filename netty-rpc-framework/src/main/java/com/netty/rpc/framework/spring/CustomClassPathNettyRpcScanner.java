package com.netty.rpc.framework.spring;

import com.netty.rpc.framework.annotation.NettyRpcService;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * 添加注册过滤条件
 *
 * @author 窦康泰
 * @date 2021/06/27
 */
public class CustomClassPathNettyRpcScanner extends ClassPathBeanDefinitionScanner {

    public CustomClassPathNettyRpcScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    public void registerFilters() {
        // 开启@Component功能
        registerDefaultFilters();
        // 将被@NettyRpcService注解的类加进Spring容器
        addIncludeFilter(new AnnotationTypeFilter(NettyRpcService.class));
    }
}
