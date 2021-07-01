package com.netty.rpc.framework.remoting.transport.netty.server;

import com.netty.rpc.common.factory.SingletonFactory;
import com.netty.rpc.common.utils.concurrent.threadpool.ThreadPoolFactoryUtils;
import com.netty.rpc.framework.config.CustomShutdownHook;
import com.netty.rpc.framework.config.RpcServiceConfig;
import com.netty.rpc.framework.provider.ServiceProvider;
import com.netty.rpc.framework.provider.impl.ZkServiceProviderImpl;
import com.netty.rpc.framework.remoting.transport.netty.codec.RpcMessageDecoder;
import com.netty.rpc.framework.remoting.transport.netty.codec.RpcMessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author 窦康泰
 * @date 2021/06/30
 */
@Component
public class NettyRpcServer {
    private static final Logger log = LoggerFactory.getLogger(NettyRpcServer.class);
    public static final int PORT = 9999;
    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);

    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig);
    }

    public void start() {
        CustomShutdownHook.clearAll();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        DefaultEventExecutorGroup eventExecutorGroup = new DefaultEventExecutorGroup(Runtime.getRuntime().availableProcessors() * 2, ThreadPoolFactoryUtils.createThreadFactory("service-handler-group", false));
        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new IdleStateHandler(10, 0, 0));
                        pipeline.addLast(new RpcMessageEncoder());
                        pipeline.addLast(new RpcMessageDecoder());
                        pipeline.addLast(eventExecutorGroup, new NettyRpcServerHandler());
                    }
                });
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(InetAddress.getLocalHost().getHostAddress(), PORT).sync();
            log.info("netty rpc server start success");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException | UnknownHostException e) {
            log.error("occur exception when start server:", e);
        } finally {
            log.error("shutdown bossGroup and workerGroup");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            eventExecutorGroup.shutdownGracefully();
        }
    }
}
