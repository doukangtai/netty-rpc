package com.netty.rpc.framework.remoting.transport.netty.client;

import com.netty.rpc.common.enums.CompressTypeEnum;
import com.netty.rpc.common.enums.RpcErrorMessageEnum;
import com.netty.rpc.common.enums.SerializationTypeEnum;
import com.netty.rpc.common.exception.RpcException;
import com.netty.rpc.common.extension.ExtensionLoader;
import com.netty.rpc.common.factory.SingletonFactory;
import com.netty.rpc.framework.registry.ServiceDiscovery;
import com.netty.rpc.framework.remoting.constants.RpcConstants;
import com.netty.rpc.framework.remoting.dto.RpcMessage;
import com.netty.rpc.framework.remoting.dto.RpcRequest;
import com.netty.rpc.framework.remoting.dto.RpcResponse;
import com.netty.rpc.framework.remoting.transport.netty.codec.RpcMessageDecoder;
import com.netty.rpc.framework.remoting.transport.netty.codec.RpcMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 窦康泰
 * @date 2021/07/01
 */
public class NettyRpcClient {
    private static final Logger log = LoggerFactory.getLogger(NettyRpcClient.class);
    private final ServiceDiscovery serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("zk");
    private final ChannelProvider channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
    private final Bootstrap bootstrap;
    private final UnprocessedRequests unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();

    public NettyRpcClient() {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap
                .group(nioEventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
//                        pipeline.addLast(new IdleStateHandler(0, 5, 0));
                        pipeline.addLast(new RpcMessageEncoder());
                        pipeline.addLast(new RpcMessageDecoder());
                        pipeline.addLast(new NettyRpcClientHandler());
                    }
                });
    }

    public CompletableFuture<RpcResponse<Object>> sendRpcRequest(RpcRequest rpcRequest) {
        CompletableFuture<RpcResponse<Object>> completableFuture = new CompletableFuture<>();
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest);
        Channel channel = getChannel(inetSocketAddress);
        if (channel != null && channel.isActive()) {
            unprocessedRequests.put(rpcRequest.getRequestId(), completableFuture);
            RpcMessage rpcMessage = new RpcMessage(RpcConstants.REQUEST_TYPE, SerializationTypeEnum.PROTOSTUFF.getCode(), CompressTypeEnum.GZIP.getCode(), ATOMIC_INTEGER.getAndIncrement(), rpcRequest);
            channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("client send message: [{}]", rpcMessage);
                } else {
                    future.channel().close();
                    completableFuture.completeExceptionally(future.cause());
                    log.error("Send failed:", future.cause());
                }
            });
        } else {
            throw new RpcException("channel not active");
        }
        return completableFuture;
    }

    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        Channel channel = channelProvider.get(inetSocketAddress);
        if (channel == null) {
            channel = doConnect(inetSocketAddress);
            channelProvider.put(inetSocketAddress, channel);
        }
        return channel;
    }

    private Channel doConnect(InetSocketAddress inetSocketAddress) {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                completableFuture.complete(future.channel());
            }
        });
        try {
            return completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RpcException(RpcErrorMessageEnum.CLIENT_CONNECT_SERVER_FAILURE);
        }
    }
}
