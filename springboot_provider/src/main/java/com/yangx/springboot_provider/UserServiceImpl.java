package com.yangx.springboot_provider;

import com.yangx.common.model.User;
import com.yangx.common.service.UserService;
import com.yangx.rpc.springboot.starter.annotation.RpcService;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 */
@RpcService
@Service
public class UserServiceImpl implements UserService {

    public User getUser(User user) {
        System.out.println("用户名: " + user.getName());
        return user;
    }
}
