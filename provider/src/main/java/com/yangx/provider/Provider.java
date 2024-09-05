package com.yangx.provider;

import com.yangx.common.service.UserService;
import com.yangx.rpc.RpcApplication;
import com.yangx.rpc.bootstrap.ProviderBootstrap;
import com.yangx.rpc.config.RegistryConfig;
import com.yangx.rpc.config.RpcConfig;
import com.yangx.rpc.constant.RpcConstant;
import com.yangx.rpc.model.ServiceMetaInfo;
import com.yangx.rpc.model.ServiceRegisterInfo;
import com.yangx.rpc.registry.LocalRegistry;
import com.yangx.rpc.registry.Registry;
import com.yangx.rpc.registry.RegistryFactory;
import com.yangx.rpc.server.VertxHttpServer;
import com.yangx.rpc.server.tcp.VertxTcpServer;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务提供者
 */
public class Provider {

    public static void main(String[] args) {

        //要注册的服务
        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo<UserService> serviceRegisterInfo = new ServiceRegisterInfo<>(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);

        //服务提供者初始化
        ProviderBootstrap.init(serviceRegisterInfoList);



//        //RPC框架初始化
//        RpcApplication.init();
//
//        //注册服务
//        String serviceName = UserService.class.getName();
//        LocalRegistry.register(serviceName, UserServiceImpl.class);
//
//        //注册服务到服务中心
//        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
//        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
//        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
//
//        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
//        serviceMetaInfo.setServiceName(serviceName);
//        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
//        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
//
//        try {
//            registry.register(serviceMetaInfo);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

        //启动web服务
//        VertxHttpServer vertxHttpServer = new VertxHttpServer();
//        vertxHttpServer.doStart(serviceMetaInfo.getServicePort());

//        VertxTcpServer vertxTcpServer = new VertxTcpServer();
//        vertxTcpServer.doStart(serviceMetaInfo.getServicePort());
    }
}
