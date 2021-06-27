package com.netty.rpc.common.extension;

import java.lang.annotation.*;

/**
 * @author 窦康泰
 * @date 2021/06/27
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {
    String value() default "";
}
