package com.netty.rpc.framework.annotation;

import java.lang.annotation.*;

/**
 * 服务引用
 *
 * @author 窦康泰
 * @date 2021/06/27
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface NettyRpcReference {
}
