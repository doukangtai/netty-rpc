package com.netty.rpc.framework.remoting.transport.netty.server;

import com.netty.rpc.common.factory.SingletonFactory;
import com.netty.rpc.framework.config.CustomShutdownHook;
import com.netty.rpc.framework.config.RpcServiceConfig;
import com.netty.rpc.framework.provider.ServiceProvider;
import com.netty.rpc.framework.provider.impl.ZkServiceProviderImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author 窦康泰
 * @date 2021/06/30
 */
public class NettyRpcServer {
    private static final Logger log = LoggerFactory.getLogger(NettyRpcServer.class);
    public static final int PORT = 9999;
    private final ServiceProvider service_provider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);

    public void registerService(RpcServiceConfig rpcServiceConfig) {
        service_provider.publishService(rpcServiceConfig);
    }

    public void start() {
        CustomShutdownHook.clearAll();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
//                        pipeline.addLast()
                    }
                });
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(InetAddress.getLocalHost().getHostAddress(), PORT).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException | UnknownHostException e) {
            log.error("occur exception when start server:", e);
        } finally {
            log.error("shutdown bossGroup and workerGroup");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
//            serviceHandlerGroup.shutdownGracefully();
        }
    }
}
