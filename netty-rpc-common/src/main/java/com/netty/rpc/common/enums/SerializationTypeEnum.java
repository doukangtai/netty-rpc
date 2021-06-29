package com.netty.rpc.common.enums;

/**
 * @author 窦康泰
 * @date 2021/06/29
 */
public enum SerializationTypeEnum {
    KYRO((byte) 0x01, "kyro"),
    PROTOSTUFF((byte) 0x02, "protostuff");;

    private final byte code;
    private final String name;

    public static String getName(byte code) {
        for (SerializationTypeEnum name : SerializationTypeEnum.values()) {
            if (name.getCode() == code) {
                return name.getName();
            }
        }
        return null;
    }

    SerializationTypeEnum(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public byte getCode() {
        return code;
    }
}
