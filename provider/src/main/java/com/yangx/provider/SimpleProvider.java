package com.yangx.provider;

import com.yangx.common.service.UserService;
import com.yangx.rpc.RpcApplication;
import com.yangx.rpc.config.RpcConfig;
import com.yangx.rpc.registry.LocalRegistry;
import com.yangx.rpc.server.HttpServer;
import com.yangx.rpc.server.VertxHttpServer;


public class SimpleProvider {

    public static void main(String[] args) {

        //框架初始化
        RpcApplication.init();

        //注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        //启动web服务
        HttpServer httpServer = new VertxHttpServer();
        int port = RpcApplication.getRpcConfig().getServerPort();
        httpServer.doStart(port);
    }
}
