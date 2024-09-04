package com.yangx.rpc.loadbalancer;

import com.yangx.rpc.registry.EtcdRegistry;
import com.yangx.rpc.registry.Registry;
import com.yangx.rpc.spi.SpiLoader;

/**
 * 负载均衡器工厂（用于获取负载均衡器）
 */
public class LoadBalancerFactory {

    static {
        SpiLoader.load(LoadBalancer.class);
    }

    /**
     * 默认负载均衡器
     */
    private static final LoadBalancer DEFAULT_LOAD_BALANCER = new RandomLoadBalancer();

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static LoadBalancer getInstance(String key) {
        return SpiLoader.getInstance(LoadBalancer.class, key);
    }
}
