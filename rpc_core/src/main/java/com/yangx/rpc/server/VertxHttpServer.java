package com.yangx.rpc.server;

import io.vertx.core.Vertx;

public class VertxHttpServer implements HttpServer {

    public void doStart(int port) {
        Vertx vertx = Vertx.vertx();

        io.vertx.core.http.HttpServer httpServer = vertx.createHttpServer();

        //监听端口并处理请求
        httpServer.requestHandler(new HttpServerHandler());

        //启动Http服务器并监听端口
        httpServer.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("HTTP server started on port " + port);
            } else {
                System.out.println("HTTP server failed to start: " + result.cause());
            }
        });
    }
}
