package com.netty.rpc.common.exception;

import com.netty.rpc.common.enums.RpcErrorMessageEnum;

/**
 * @author 窦康泰
 * @date 2021/06/29
 */
public class RpcException extends RuntimeException {
    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum, String message) {
        super(rpcErrorMessageEnum.getMessage() + ":" + message);
    }

    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum) {
        super(rpcErrorMessageEnum.getMessage());
    }
}
