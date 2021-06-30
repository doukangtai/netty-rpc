package com.netty.rpc.framework.compress;

import com.netty.rpc.common.extension.SPI;

/**
 * @author 窦康泰
 * @date 2021/06/30
 */
@SPI
public interface Compress {
    byte[] compress(byte[] bytes);

    byte[] decompress(byte[] bytes);
}
