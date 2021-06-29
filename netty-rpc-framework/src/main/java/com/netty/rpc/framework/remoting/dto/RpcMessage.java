package com.netty.rpc.framework.remoting.dto;

/**
 * @author 窦康泰
 * @date 2021/06/29
 */
public class RpcMessage {
    private byte messageType;
    private byte codec;
    private byte compress;
    private int requestId;
    private Object data;

    public RpcMessage() {
    }

    public RpcMessage(byte messageType, byte codec, byte compress, int requestId, Object data) {
        this.messageType = messageType;
        this.codec = codec;
        this.compress = compress;
        this.requestId = requestId;
        this.data = data;
    }

    public byte getMessageType() {
        return messageType;
    }

    public void setMessageType(byte messageType) {
        this.messageType = messageType;
    }

    public byte getCodec() {
        return codec;
    }

    public void setCodec(byte codec) {
        this.codec = codec;
    }

    public byte getCompress() {
        return compress;
    }

    public void setCompress(byte compress) {
        this.compress = compress;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RpcMessage{" +
                "messageType=" + messageType +
                ", codec=" + codec +
                ", compress=" + compress +
                ", requestId=" + requestId +
                ", data=" + data +
                '}';
    }
}
