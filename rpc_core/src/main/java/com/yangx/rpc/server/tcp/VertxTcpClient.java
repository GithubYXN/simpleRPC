package com.yangx.rpc.server.tcp;

import com.yangx.rpc.model.RpcRequest;
import com.yangx.rpc.model.RpcResponse;
import com.yangx.rpc.model.ServiceMetaInfo;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;

import java.util.concurrent.ExecutionException;

/**
 * Vertx TCP 请求客户端
 */
public class VertxTcpClient {

    public void start() {
        Vertx vertx = Vertx.vertx();

        vertx.createNetClient().connect(8848, "localhost", result -> {
            if (result.succeeded()) {

                System.out.println("Connected to TCP server");
                io.vertx.core.net.NetSocket socket = result.result();

                //发送数据
                socket.write("Hello Server!");

//                //测试
//                for (int i = 0; i < 1000; i++) {
//                    socket.write("Hello Server! Hello World! Hello Server! Hello World!");
//                }

                //接受响应
                socket.handler(buffer -> {
                    System.out.println("Received response from Server: " + buffer.toString());
                });
            } else {
                System.out.println("Failed to connect to TCP server");
            }
        });
    }

    public static void main(String[] args) {
        VertxTcpClient client = new VertxTcpClient();
        client.start();
    }
}
