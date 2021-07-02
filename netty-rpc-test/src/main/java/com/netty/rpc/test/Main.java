package com.netty.rpc.test;

import com.netty.rpc.framework.loadbalance.LoadBalance;
import com.netty.rpc.framework.loadbalance.loadbalancer.LFULoadBalance;
import com.netty.rpc.framework.remoting.dto.RpcRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author 窦康泰
 * @date 2021/06/28
 */
public class Main {
    public static void main(String[] args) {
        LoadBalance loadBalance = new LFULoadBalance();
//        LoadBalance loadBalance = new RoundRobinLoadBalance();
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        RpcRequest rpcRequest = new RpcRequest(UUID.randomUUID().toString(), "interfaceName1", "", new Object[0], new Class<?>[0], "version1", "group1");
        for (int i = 0; i < 10; i++) {
            String address = loadBalance.selectServiceAddress(list, rpcRequest);
            System.out.println(address);
        }
        List<String> list1 = new ArrayList<>();
        list1.add("4");
        list1.add("5");
        list1.add("6");
        list1.add("7");
        list1.add("8");
        System.out.println("-------------------");
        for (int i = 0; i < 10; i++) {
            String address = loadBalance.selectServiceAddress(list1, rpcRequest);
            System.out.println(address);
        }
    }
}
