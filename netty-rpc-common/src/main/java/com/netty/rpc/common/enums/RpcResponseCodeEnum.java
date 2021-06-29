package com.netty.rpc.common.enums;

/**
 * @author 窦康泰
 * @date 2021/06/29
 */
public enum RpcResponseCodeEnum {
    SUCCESS(200, "The remote call is successful"),
    FAIL(500, "The remote call is fail");
    private final int code;

    private final String message;

    RpcResponseCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "RpcResponseCodeEnum{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
