package com.yangx.rpc.server.tcp;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;
import lombok.extern.slf4j.Slf4j;

/**
 * Vertex TCP服务器
 */
@Slf4j
public class VertxTcpServer {

    private byte[] handleRequest(byte[] requestData) {
        return "Hello Client!".getBytes();
    }

    public void doStart(int port) {
        //创建Vert.x实例
        Vertx vertx = Vertx.vertx();

        //创建TCP服务器
        NetServer server = vertx.createNetServer();

        //处理请求
        server.connectHandler(new TcpServerHandler());

//        //测试
//        server.connectHandler(netSocket -> {
//
//            String testMessage = "Hello Server! Hello World! Hello Server! Hello World!";
//            int messageLength = testMessage.getBytes().length;
//
//            //构造Parser
//            RecordParser parser = RecordParser.newFixed(messageLength);
//            parser.setOutput(new Handler<Buffer>() {
//                @Override
//                public void handle(Buffer buffer) {
//                    String str = new String(buffer.getBytes());
//                    System.out.println(str);
//
//                    if(testMessage.equals(str)) {
//                        System.out.println("Good.");
//                    }
//                }
//            });
//                netSocket.handler(parser);
//        });



        //启动TCP服务器并监听指定端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                log.info("TCP Server started on port {}", port);
            } else {
                log.error("TCP　Server start failed", result.cause());
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(8848);
    }
}
