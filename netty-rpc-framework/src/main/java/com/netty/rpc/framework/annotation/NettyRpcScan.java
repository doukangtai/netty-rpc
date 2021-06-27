package com.netty.rpc.framework.annotation;

import com.netty.rpc.framework.spring.CustomNettyRpcScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 仿照 mybatis-spring-boot-starter.version -> 2.1.3 的 MapperScan 实现的扫描器
 *
 * @author 窦康泰
 * @date 2021/06/27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({CustomNettyRpcScannerRegistrar.class})
public @interface NettyRpcScan {

    String[] basePackages() default {};

}
