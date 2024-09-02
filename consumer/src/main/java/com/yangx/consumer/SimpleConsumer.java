package com.yangx.consumer;

import com.yangx.common.model.User;
import com.yangx.common.service.UserService;
import com.yangx.simplerpc.proxy.ServiceProxyFactory;

public class SimpleConsumer {
    public static void main(String[] args) {
        //获取UserService实现类
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("yangx666");

        //调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user is null");
        }
    }
}
