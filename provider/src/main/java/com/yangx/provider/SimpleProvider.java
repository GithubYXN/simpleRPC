package com.yangx.provider;

import com.yangx.common.service.UserService;
import com.yangx.simplerpc.registry.LocalRegistry;
import com.yangx.simplerpc.server.HttpServer;
import com.yangx.simplerpc.server.VertxHttpServer;

public class SimpleProvider {

    public static void main(String[] args) {

        //注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        //启动web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}
