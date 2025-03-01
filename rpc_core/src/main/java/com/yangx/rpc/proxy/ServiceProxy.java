package com.yangx.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.yangx.rpc.RpcApplication;
import com.yangx.rpc.config.RpcConfig;
import com.yangx.rpc.constant.RpcConstant;
import com.yangx.rpc.fault.retry.RetryStrategy;
import com.yangx.rpc.fault.retry.RetryStrategyFactory;
import com.yangx.rpc.fault.tolerant.TolerantStrategy;
import com.yangx.rpc.fault.tolerant.TolerantStrategyFactory;
import com.yangx.rpc.loadbalancer.LoadBalancer;
import com.yangx.rpc.loadbalancer.LoadBalancerFactory;
import com.yangx.rpc.model.RpcRequest;
import com.yangx.rpc.model.RpcResponse;
import com.yangx.rpc.model.ServiceMetaInfo;
import com.yangx.rpc.protocol.*;
import com.yangx.rpc.registry.Registry;
import com.yangx.rpc.registry.RegistryFactory;
import com.yangx.rpc.serializer.JdkSerializer;
import com.yangx.rpc.serializer.Serializer;
import com.yangx.rpc.serializer.SerializerFactory;
import com.yangx.rpc.server.tcp.VertxTcpClient;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 *服务代理（JDK动态代理）
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //构造请求
        String serviceName = method.getDeclaringClass().getName();

        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        // 从注册中心获取服务提供者请求地址
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
        if (CollUtil.isEmpty(serviceMetaInfoList)) {
            throw new RuntimeException("暂无服务地址");
        }

        // 负载均衡
        LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
        //将调用方法名作为负载均衡参数
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("methodName", method.getName());
        ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
        //测试
        System.out.println("负载均衡器: " + rpcConfig.getLoadBalancer());
        System.out.println("本次请求的端口是: " + selectedServiceMetaInfo.getServicePort());

        //发送TCP请求
        RpcResponse rpcResponse;

        try {
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
            rpcResponse = retryStrategy.doRetry(() ->
                    VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo)
            );
        } catch (Exception e) {
            //容错机制
            TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
            rpcResponse = tolerantStrategy.doTolerant(null, e);
        }
        return rpcResponse.getData();

//        //发送TCP请求
//        Vertx vertx = Vertx.vertx();
//        NetClient netClient = vertx.createNetClient();
//
//        CompletableFuture<Object> responseFuture = new CompletableFuture<>();
//
//        netClient.connect(selectedServiceMetaInfo.getServicePort(), selectedServiceMetaInfo.getServiceHost(), result -> {
//            if (result.succeeded()) {
//                System.out.println("Connected to TCP server.");
//
//                io.vertx.core.net.NetSocket socket = result.result();
//
//                // 发送数据
//                // 构造消息
//
//                ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
//                ProtocolMessage.Header header = new ProtocolMessage.Header();
//
//                header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
//                header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
//                header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
//                header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
//                header.setRequestId(IdUtil.getSnowflakeNextId());
//
//                protocolMessage.setHeader(header);
//                protocolMessage.setBody(rpcRequest);
//
//                // 编码请求
//                try {
//
//                    Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
//                    socket.write(encodeBuffer);
//                } catch (Exception e) {
//                    throw new RuntimeException("协议消息编码错误");
//                }
//
//                // 接收响应
//                socket.handler(buffer -> {
//                    try {
//
//                        ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
//                        responseFuture.complete(rpcResponseProtocolMessage.getBody());
//                    } catch (IOException e) {
//                        throw new RuntimeException("协议消息解码错误");
//                    }
//                });
//
//            } else {
//
//                System.err.println("Failed to connect to TCP server.");
//            }
//        });
//
//
//        RpcResponse rpcResponse = (RpcResponse) responseFuture.get();
//
//        // 记得关闭连接
//        netClient.close();
//        return rpcResponse.getData();
    }
}



//        // 指定序列化器
//        Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
//
//        String serviceName = method.getDeclaringClass().getName();
//
//        // 构造请求
//        RpcRequest rpcRequest = RpcRequest.builder()
//                .serviceName(method.getDeclaringClass().getName())
//                .methodName(method.getName())
//                .parameterTypes(method.getParameterTypes())
//                .args(args)
//                .build();
//        try {
//            // 序列化
//            byte[] bodyBytes = serializer.serialize(rpcRequest);
//
//            // 从注册中心获取服务提供者请求地址
//            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
//            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
//            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
//            serviceMetaInfo.setServiceName(serviceName);
//            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
//            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
//            if (CollUtil.isEmpty(serviceMetaInfoList)) {
//                throw new RuntimeException("暂无服务地址");
//            }
//
//            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);
//
//            try (HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
//                    .body(bodyBytes)
//                    .execute()) {
//                byte[] result = httpResponse.bodyBytes();
//                // 反序列化
//                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
//                return rpcResponse.getData();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;