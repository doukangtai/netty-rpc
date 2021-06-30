package com.netty.rpc.framework.remoting.transport.netty.server;

import com.netty.rpc.common.factory.SingletonFactory;
import com.netty.rpc.framework.remoting.constants.RpcConstants;
import com.netty.rpc.framework.remoting.dto.RpcMessage;
import com.netty.rpc.framework.remoting.dto.RpcRequest;
import com.netty.rpc.framework.remoting.dto.RpcResponse;
import com.netty.rpc.framework.remoting.handler.RpcRequestHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 窦康泰
 * @date 2021/06/30
 */
public class NettyRpcServerHandler extends SimpleChannelInboundHandler<RpcMessage> {
    private static final Logger log = LoggerFactory.getLogger(NettyRpcServerHandler.class);
    private final RpcRequestHandler rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage msg) throws Exception {
        try {
            log.info("server receive msg: [{}] ", msg);
            RpcMessage rpcMessage = new RpcMessage();
            rpcMessage.setRequestId(msg.getRequestId());
            rpcMessage.setCodec(msg.getCodec());
            rpcMessage.setCompress(msg.getCompress());
            byte messageType = msg.getMessageType();
            if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
                rpcMessage.setMessageType(RpcConstants.HEARTBEAT_RESPONSE_TYPE);
                rpcMessage.setData(RpcConstants.PONG);
            } else if (messageType == RpcConstants.REQUEST_TYPE) {
                rpcMessage.setMessageType(RpcConstants.RESPONSE_TYPE);
                Object result = rpcRequestHandler.handle((RpcRequest) msg.getData());
                RpcResponse<Object> rpcResponse;
                if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                    rpcResponse = RpcResponse.success(result, ((RpcRequest) msg.getData()).getRequestId());
                } else {
                    log.error("not writable now, message dropped");
                    rpcResponse = RpcResponse.fail();
                }
                rpcMessage.setData(rpcResponse);
            }
            ctx.channel().writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
