package com.netty.rpc.framework.spring;

import com.netty.rpc.framework.annotation.NettyRpcScan;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 获取@NettyRpcScan注解的信息，关联CustomNettyRpcScannerConfigurer类
 *
 * @author 窦康泰
 * @date 2021/06/27
 */
public class CustomNettyRpcScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes nettyRpcScanAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(NettyRpcScan.class.getName()));
        if (nettyRpcScanAttributes != null) {
            registerBeanDefinitions(importingClassMetadata, nettyRpcScanAttributes, registry, generateBaseBeanName(importingClassMetadata, 0));
        }
    }

    void registerBeanDefinitions(AnnotationMetadata annoMeta, AnnotationAttributes annoAttrs, BeanDefinitionRegistry registry, String beanName) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(CustomNettyRpcScannerConfigurer.class);
        List<String> basePackages = new ArrayList<>();
        basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText).collect(Collectors.toList()));
        if (basePackages.isEmpty()) {
            basePackages.add(getDefaultBasePackage(annoMeta));
        }
        builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(basePackages));
        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
    }

    private static String generateBaseBeanName(AnnotationMetadata importingClassMetadata, int index) {
        return importingClassMetadata.getClassName() + "#" + CustomNettyRpcScannerRegistrar.class.getSimpleName() + "#" + index;
    }

    private static String getDefaultBasePackage(AnnotationMetadata importingClassMetadata) {
        return ClassUtils.getPackageName(importingClassMetadata.getClassName());
    }
}
