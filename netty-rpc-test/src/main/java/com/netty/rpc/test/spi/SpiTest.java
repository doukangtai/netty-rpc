package com.netty.rpc.test.spi;

import com.netty.rpc.api.spi.SpiService;
import com.netty.rpc.common.extension.ExtensionLoader;

/**
 * @author 窦康泰
 * @date 2021/06/27
 */
public class SpiTest {
    public static void main(String[] args) {
        ExtensionLoader<SpiService> extensionLoader = ExtensionLoader.getExtensionLoader(SpiService.class);
        SpiService spiService = extensionLoader.getExtension("spiService2");
        spiService.say("Hello World!!!");
    }
}
