package com.netty.rpc.framework.remoting.transport.netty.codec;

import com.netty.rpc.common.enums.CompressTypeEnum;
import com.netty.rpc.common.enums.SerializationTypeEnum;
import com.netty.rpc.common.exception.CodecException;
import com.netty.rpc.common.extension.ExtensionLoader;
import com.netty.rpc.framework.compress.Compress;
import com.netty.rpc.framework.remoting.constants.RpcConstants;
import com.netty.rpc.framework.remoting.dto.RpcMessage;
import com.netty.rpc.framework.remoting.dto.RpcRequest;
import com.netty.rpc.framework.remoting.dto.RpcResponse;
import com.netty.rpc.framework.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.Arrays;

/**
 * 0     1     2     3     4       5    6    7    8    9          10      11       12    13    14   15   16
 * +-----+-----+-----+-----+-------+----+----+----+----+-----------+-------+--------+-----+-----+----+----+
 * |   magic   code        |version|   full length     |messageType| codec |compress|     RequestId       |
 * +-----------------------+-------+-------------------+-----------+-------+--------+-----+-----+----+----+
 * |                                                                                                      |
 * |                                         body                                                         |
 * |                                                                                                      |
 * |                                        ... ...                                                       |
 * +------------------------------------------------------------------------------------------------------+
 * 4B  magic code（魔法数）   1B version（版本）   4B full length（消息长度）    1B messageType（消息类型）
 * 1B compress（压缩类型） 1B codec（序列化类型）    4B  requestId（请求的Id）
 * body（object类型数据）
 *
 * @author 窦康泰
 * @date 2021/06/30
 */
public class RpcMessageDecoder extends LengthFieldBasedFrameDecoder {
    public RpcMessageDecoder() {
        super(RpcConstants.MAX_FRAME_LENGTH, 5, 4, -9, 0);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        in = (ByteBuf) super.decode(ctx, in);
        if (in == null) {
            return null;
        }
        if (in.readableBytes() < RpcConstants.HEAD_LENGTH) {
            throw new CodecException("decode exception");
        }
        checkMagicNumber(in);
        checkVersion(in);
        int fullLength = in.readInt();
        byte messageType = in.readByte();
        byte codecCode = in.readByte();
        byte compressCode = in.readByte();
        int requestId = in.readInt();
        if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
            return new RpcMessage(messageType, codecCode, compressCode, requestId, RpcConstants.PING);
        } else if (messageType == RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
            return new RpcMessage(messageType, codecCode, compressCode, requestId, RpcConstants.PONG);
        }
        int bodyLength = fullLength - RpcConstants.HEAD_LENGTH;
        if (bodyLength <= 0) {
            return new RpcMessage(messageType, codecCode, compressCode, requestId, null);
        }
        byte[] body = new byte[bodyLength];
        in.readBytes(body);
        String compressName = CompressTypeEnum.getName(compressCode);
        Compress compress = ExtensionLoader.getExtensionLoader(Compress.class).getExtension(compressName);
        String serializerName = SerializationTypeEnum.getName(codecCode);
        Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getExtension(serializerName);
        body = compress.decompress(body);
        if (messageType == RpcConstants.REQUEST_TYPE) {
            RpcRequest rpcRequest = serializer.deserialize(body, RpcRequest.class);
            return new RpcMessage(messageType, codecCode, compressCode, requestId, rpcRequest);
        } else {
            RpcResponse response = serializer.deserialize(body, RpcResponse.class);
            return new RpcMessage(messageType, codecCode, compressCode, requestId, response);
        }
    }

    private void checkMagicNumber(ByteBuf in) {
        byte[] magicNumber = RpcConstants.MAGIC_NUMBER;
        byte[] bytes = new byte[magicNumber.length];
        in.readBytes(bytes);
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] != magicNumber[i]) {
                throw new IllegalArgumentException("Unknown magic code: " + Arrays.toString(bytes));
            }
        }
    }

    private void checkVersion(ByteBuf in) {
        byte version = in.readByte();
        if (version != RpcConstants.VERSION) {
            throw new RuntimeException("version isn't compatible" + version);
        }
    }
}
