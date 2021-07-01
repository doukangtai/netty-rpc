package com.netty.rpc.framework.config;

/**
 * @author 窦康泰
 * @date 2021/06/29
 */
public class RpcServiceConfig {
    private String version = "";
    private String group = "";
    private Object service;

    public String getRpcServiceName() {
        return this.getServiceName() + this.getGroup() + this.getVersion();
    }

    public String getServiceName() {
        return this.service.getClass().getInterfaces()[0].getCanonicalName();
    }

    public RpcServiceConfig() {
    }

    public RpcServiceConfig(String version, String group) {
        this.version = version;
        this.group = group;
    }

    public RpcServiceConfig(String version, String group, Object service) {
        this.version = version;
        this.group = group;
        this.service = service;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Object getService() {
        return service;
    }

    public void setService(Object service) {
        this.service = service;
    }

    @Override
    public String toString() {
        return "RpcServiceConfig{" +
                "version='" + version + '\'' +
                ", group='" + group + '\'' +
                ", service=" + service +
                '}';
    }
}
