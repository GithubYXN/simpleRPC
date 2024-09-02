package com.yangx.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.yangx.common.model.User;
import com.yangx.common.service.UserService;
import com.yangx.rpc.model.RpcRequest;
import com.yangx.rpc.model.RpcResponse;
import com.yangx.rpc.serializer.JdkSerializer;
import com.yangx.rpc.serializer.Serializer;

import java.io.IOException;

public class UserServiceProxy implements UserService {

    public User getUser(User user) {
        //指定序列化器
        final Serializer serializer = new JdkSerializer();

        //构造请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})
                .args(new Object[]{user})
                .build();
        try {
            byte[] bodyBytes = serializer.serialize(rpcRequest);

            //发送请求
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                    .body(bodyBytes)
                    .execute()) {
                byte[] result = httpResponse.bodyBytes();
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return (User) rpcResponse.getData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
