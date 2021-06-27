package com.netty.rpc.api.spi;

import com.netty.rpc.common.extension.SPI;

/**
 * @author 窦康泰
 * @date 2021/06/27
 */
@SPI
public interface SpiService {
    void say(String msg);
}
