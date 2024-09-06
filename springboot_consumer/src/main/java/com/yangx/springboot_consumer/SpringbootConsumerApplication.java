package com.yangx.springboot_consumer;

import com.yangx.rpc.springboot.starter.annotation.EnableRpc;
import com.yangx.rpc.springboot.starter.annotation.RpcReference;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRpc(needServer = false)
public class SpringbootConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootConsumerApplication.class, args);
    }

}
