package com.netty.rpc.framework.config;

import com.netty.rpc.framework.registry.zk.util.CuratorUtils;
import com.netty.rpc.framework.remoting.transport.netty.server.NettyRpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * @author 窦康泰
 * @date 2021/06/29
 */
public class CustomShutdownHook {
    private static final Logger log = LoggerFactory.getLogger(CustomShutdownHook.class);

    public static void clearAll() {
        log.info("add ShutdownHook for clearAll");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            InetSocketAddress inetSocketAddress = null;
            try {
                inetSocketAddress = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), NettyRpcServer.PORT);
            } catch (UnknownHostException e) {
                log.error(e.getMessage());
            }
            CuratorUtils.clearRegistry(CuratorUtils.getZkClient(), inetSocketAddress);
        }));
    }
}
