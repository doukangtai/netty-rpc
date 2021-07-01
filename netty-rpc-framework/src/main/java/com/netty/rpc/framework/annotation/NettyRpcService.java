package com.netty.rpc.framework.annotation;

import java.lang.annotation.*;

/**
 * 服务暴露
 *
 * @author 窦康泰
 * @date 2021/06/27
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface NettyRpcService {
    String version() default "";

    String group() default "";
}
