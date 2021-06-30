package com.netty.rpc.framework.remoting.transport.netty.codec;

import com.netty.rpc.common.enums.CompressTypeEnum;
import com.netty.rpc.common.enums.SerializationTypeEnum;
import com.netty.rpc.common.extension.ExtensionLoader;
import com.netty.rpc.framework.compress.Compress;
import com.netty.rpc.framework.remoting.constants.RpcConstants;
import com.netty.rpc.framework.remoting.dto.RpcMessage;
import com.netty.rpc.framework.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {
    private static final Logger log = LoggerFactory.getLogger(RpcMessageEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage msg, ByteBuf out) throws Exception {
        try {
            out.writeBytes(RpcConstants.MAGIC_NUMBER);
            out.writeByte(RpcConstants.VERSION);
            out.writerIndex(out.writerIndex() + 4);
            out.writeByte(msg.getMessageType());
            out.writeByte(msg.getCodec());
            out.writeByte(msg.getCompress());
            out.writeInt(msg.getRequestId());
            byte[] body = null;
            if (msg.getMessageType() != RpcConstants.HEARTBEAT_RESPONSE_TYPE && msg.getMessageType() != RpcConstants.HEARTBEAT_REQUEST_TYPE) {
                Object data = msg.getData();
                String codecName = SerializationTypeEnum.getName(msg.getCodec());
                Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getExtension(codecName);
                String compressName = CompressTypeEnum.getName(msg.getCompress());
                Compress compress = ExtensionLoader.getExtensionLoader(Compress.class).getExtension(compressName);
                body = serializer.serialize(data);
                body = compress.compress(body);
            }
            if (body != null) {
                out.writeBytes(body);
            }
            int fullLength = body.length + RpcConstants.HEAD_LENGTH;
            int index = out.writerIndex() - fullLength + RpcConstants.MAGIC_NUMBER.length + 1;
            out.writerIndex(index);
            out.writeInt(fullLength);
            out.writerIndex(fullLength);
        } catch (Exception e) {
            log.error("Encode request error!", e);
        }
    }
}
