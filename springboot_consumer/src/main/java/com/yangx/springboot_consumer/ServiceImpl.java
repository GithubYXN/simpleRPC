package com.yangx.springboot_consumer;

import com.yangx.common.model.User;
import com.yangx.common.service.UserService;
import com.yangx.rpc.springboot.starter.annotation.RpcReference;
import com.yangx.rpc.springboot.starter.annotation.RpcService;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 */
@Service
public class ServiceImpl {

    /**
     * 使用RPC框架注入
     */
    @RpcReference
    private UserService userService;

    /**
     * 测试方法
     */
    public void test() {
        User user = new User();
        user.setName("yangx");
        User resultUser = userService.getUser(user);
        System.out.println(resultUser.getName());
    }
}
