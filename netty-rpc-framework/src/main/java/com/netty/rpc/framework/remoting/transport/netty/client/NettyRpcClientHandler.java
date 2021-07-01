package com.netty.rpc.framework.remoting.transport.netty.client;

import com.netty.rpc.common.enums.CompressTypeEnum;
import com.netty.rpc.common.enums.SerializationTypeEnum;
import com.netty.rpc.common.factory.SingletonFactory;
import com.netty.rpc.framework.remoting.constants.RpcConstants;
import com.netty.rpc.framework.remoting.dto.RpcMessage;
import com.netty.rpc.framework.remoting.dto.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 窦康泰
 * @date 2021/07/01
 */
public class NettyRpcClientHandler extends SimpleChannelInboundHandler<RpcMessage> {
    private static final Logger log = LoggerFactory.getLogger(NettyRpcClientHandler.class);
    private final UnprocessedRequests unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage msg) throws Exception {
        if (msg.getMessageType() == RpcConstants.RESPONSE_TYPE) {
            log.info("client receive msg: [{}] ", msg);
            unprocessedRequests.complete((RpcResponse<Object>) msg.getData());
        } else if (msg.getMessageType() == RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
            log.info("client receive heart beat msg: [{}]", msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                log.info("writer idle happen");
                RpcMessage rpcMessage = new RpcMessage(RpcConstants.HEARTBEAT_REQUEST_TYPE, SerializationTypeEnum.PROTOSTUFF.getCode(), CompressTypeEnum.GZIP.getCode(), 0, RpcConstants.PING);
                ctx.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                log.info("client send heart beat msg: [{}] ", rpcMessage);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("client catch exception", cause);
        ctx.close();
    }
}
