package com.netty.rpc.framework.loadbalance;

import com.netty.rpc.common.extension.SPI;
import com.netty.rpc.framework.remoting.dto.RpcRequest;

import java.util.List;

/**
 * @author 窦康泰
 * @date 2021/06/29
 */
@SPI
public interface LoadBalance {
    String selectServiceAddress(List<String> serviceAddresses, RpcRequest rpcRequest);
}
