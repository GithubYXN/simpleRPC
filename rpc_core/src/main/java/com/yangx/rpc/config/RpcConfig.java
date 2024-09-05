package com.yangx.rpc.config;

import com.yangx.rpc.fault.retry.RetryStrategyKeys;
import com.yangx.rpc.fault.tolerant.TolerantStrategyKeys;
import com.yangx.rpc.loadbalancer.LoadBalancerKeys;
import com.yangx.rpc.serializer.SerializerKeys;
import lombok.Data;

/**
 * RPC 框架全局设置
 */
@Data
public class RpcConfig {

    /**
     * 模拟调用
     */
    private boolean mock = false;

    /**
     * 名称
     */
    private String name = "rpc-core";

    /**
     * 版本号
     */
    private String version = "1.0.0";

    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";

    /**
     * 服务器端口号
     */
    private Integer serverPort = 8888;

    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();

    /**
     * 负载均衡器
     */
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;

    /**
     * 重试策略
     */
    public String retryStrategy = RetryStrategyKeys.NO;

    /**
     * 容错策略
     */
    public String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;
}
