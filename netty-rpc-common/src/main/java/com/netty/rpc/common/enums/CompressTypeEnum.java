package com.netty.rpc.common.enums;

/**
 * @author 窦康泰
 * @date 2021/06/29
 */
public enum CompressTypeEnum {
    GZIP((byte) 1, "gzip");

    private final byte code;
    private final String name;

    public static String getName(byte code) {
        for (CompressTypeEnum value : CompressTypeEnum.values()) {
            if (value.getCode() == code) {
                return value.getName();
            }
        }
        return null;
    }

    CompressTypeEnum(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public byte getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
