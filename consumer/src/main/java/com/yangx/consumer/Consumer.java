package com.yangx.consumer;

import com.yangx.common.model.User;
import com.yangx.common.service.UserService;
import com.yangx.rpc.RpcApplication;
import com.yangx.rpc.config.RpcConfig;
import com.yangx.rpc.constant.RpcConstant;
import com.yangx.rpc.proxy.ServiceProxyFactory;
import com.yangx.rpc.utils.ConfigUtils;

/**
 * 服务消费者示例
 */
public class Consumer {

    public static void main(String[] args) {

//        RpcConfig rpc = RpcApplication.getRpcConfig();
//        System.out.println(rpc);

        //获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);

        User user = new User();
        user.setName("yangx123");

        //调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user is null");
        }

        long number = userService.getNumber();
        System.out.println(number);
    }
}
