package com.netty.rpc.framework.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

/**
 * 注册过滤条件，根据basePackage的内容进行自定义注解扫描，加入Spring容器
 *
 * @author 窦康泰
 * @date 2021/06/27
 */
public class CustomNettyRpcScannerConfigurer implements BeanDefinitionRegistryPostProcessor {
    // 自动注入@NettyRpcScan注解的basePackages信息
    private String basePackage;
    private static final String SPRING_COMPONENT_SCAN_BASE_PACKAGE = "com.netty.rpc";

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        CustomClassPathNettyRpcScanner scanner = new CustomClassPathNettyRpcScanner(beanDefinitionRegistry);
        scanner.registerFilters();
        scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
        // 扫描自定义RPC框架下的包，配合CustomClassPathNettyRpcScanner -> registerFilters() -> registerDefaultFilters()实现@Component装载Bean的功能
        scanner.scan(SPRING_COMPONENT_SCAN_BASE_PACKAGE);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        // left intentionally blank
    }
}
